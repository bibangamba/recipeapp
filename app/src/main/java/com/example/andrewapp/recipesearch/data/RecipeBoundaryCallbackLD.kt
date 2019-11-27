package com.example.andrewapp.recipesearch.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.andrewapp.db.RecipeDao
import com.example.andrewapp.db.RecipeEntity
import com.example.andrewapp.service.RecipeAPIService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Handles boundary callbacks. When the db doesn't have requested data or
 * when data available in the database has been exhausted, the boundary callback is triggered.
 *
 * @param query recipe search term
 * @param service api service used for network calls
 * @param dao room data access object used to save recipes into the database
 */
class RecipeBoundaryCallbackLD(
    private val query: String,
    private val service: RecipeAPIService,
    private val dao: RecipeDao
) : PagedList.BoundaryCallback<RecipeEntity>() {
    companion object {
        private const val NETWORK_PAGE_SIZE = 50
        private const val INITIAL_NETWORK_PAGE_SIZE = 10
    }

    // save total number of available recipe results.
    // used to prevent recursive search when api search results are exhausted.
    // (fix for bug in API pagination)
    private var mTotalResultsAvailableFromNetwork = 0

    private var mPaginationOffset = 0

    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    private val disposables = CompositeDisposable()
    private val _disposablesLiveData =
        MutableLiveData<CompositeDisposable>()
    val disposablesLiveData: LiveData<CompositeDisposable>
        get() = _disposablesLiveData

    /**
     * ensures we don't trigger multiple requests at the same time.
     */
    private var isRequestInProgress = false

    /**
     * Triggered when zero items returned from the db
     * i.e. no match found in local db or db is empty
     *
     * Make request to API to search for match
     */
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        requestAndSaveRecipes()
    }

    /**
     * Triggered when all items in database have been retrieved.
     * We need to request API for more data
     */
    override fun onItemAtEndLoaded(itemAtEnd: RecipeEntity) {
        super.onItemAtEndLoaded(itemAtEnd)
        requestAndSaveRecipes()
    }

    /**
     * request for more data (from the network) and save it into the database
     */
    private fun requestAndSaveRecipes() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        if (mPaginationOffset == 0) {
            apiRequestRecipes(query, INITIAL_NETWORK_PAGE_SIZE, mPaginationOffset)
        } else if (mPaginationOffset < mTotalResultsAvailableFromNetwork) {
            /**
             *  continue requesting only if mTotalResultsAvailableFromNetwork has not been exceeded
             */
            apiRequestRecipes(query, NETWORK_PAGE_SIZE, mPaginationOffset)
        }

    }

    /**
     * makes triggers requests to api when searching for recipe
     *
     * @param query recipe search term
     * @param networkPageSize number of results to return on this request
     * @param offset how many recipes to skip when paginating (offset pagination)
     *
     */
    private fun apiRequestRecipes(query: String, networkPageSize: Int, offset: Int) {
        disposables.add(
            service.getRecipes(query, networkPageSize, offset)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (networkPageSize == INITIAL_NETWORK_PAGE_SIZE) {
                        /**
                         *  accurate `totalResults` are only available on the initial request
                         */
                        mTotalResultsAvailableFromNetwork = response.totalResults
                    }
                    saveRecipes(response.recipes)
                }, { error ->
                    _networkErrors.postValue(error.message)
                    isRequestInProgress = false
                })
        )
        _disposablesLiveData.postValue(disposables)
    }

    /**
     * saves recipes to sqlite database using room
     * @param recipes list of recipes to save into the database
     */
    private fun saveRecipes(recipes: List<RecipeEntity>) {
        disposables.add(
            dao.insert(recipes)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    mPaginationOffset += recipes.size
                    isRequestInProgress = false
                }
        )
        _disposablesLiveData.postValue(disposables)
    }
}
