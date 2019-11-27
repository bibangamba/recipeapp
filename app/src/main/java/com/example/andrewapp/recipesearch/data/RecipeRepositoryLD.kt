package com.example.andrewapp.recipesearch.data

import androidx.paging.LivePagedListBuilder
import com.example.andrewapp.db.RecipeDao
import com.example.andrewapp.model.RecipeSearchResult
import com.example.andrewapp.service.RecipeAPIService
import javax.inject.Inject

open class RecipeRepositoryLD @Inject constructor(
    private val service: RecipeAPIService,
    private val dao: RecipeDao
) {
    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }

    /**
     * search for recipes in the database or the network
     *
     * @param query recipe search term
     *
     * @return RecipeSearchResult search result object that contains:
     *         returned recipes OR an error, and a CompositeDisposable
     *         to clear in the ViewModel layer
     */
    fun search(query: String): RecipeSearchResult {
        val wildcardsQuery = "%${query.replace(" ", "%")}%"
        val dataSourceFactory = dao.searchRecipes(wildcardsQuery)

        val boundaryCallback = RecipeBoundaryCallbackLD(query, service, dao)

        val networkErrors = boundaryCallback.networkErrors
        val disposables = boundaryCallback.disposablesLiveData

        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return RecipeSearchResult(data, networkErrors, disposables)
    }
}

