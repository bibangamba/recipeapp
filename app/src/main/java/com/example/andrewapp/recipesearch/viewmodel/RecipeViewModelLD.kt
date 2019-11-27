package com.example.andrewapp.recipesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.andrewapp.db.RecipeEntity
import com.example.andrewapp.model.RecipeSearchResult
import com.example.andrewapp.recipesearch.data.RecipeRepositoryLD
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class RecipeViewModelLD @Inject constructor(private val recipeRepositoryLD: RecipeRepositoryLD) :
    ViewModel() {
    private val queryAsLiveData = MutableLiveData<String>()

    /**
     * recipeRepositoryLD.search is triggered when queryAsLiveData gets updated
     */
    private val recipeResult: LiveData<RecipeSearchResult> =
        Transformations.map(queryAsLiveData) {
            recipeRepositoryLD.search(it)
        }

    val recipes: LiveData<PagedList<RecipeEntity>> =
        Transformations.switchMap(recipeResult) { it.data }

    val networkErrors: LiveData<String> =
        Transformations.switchMap(recipeResult) { it.networkErrors }

    val disposables: LiveData<CompositeDisposable> =
        Transformations.switchMap(recipeResult) { it.disposables }

    /**
     * trigger search by updating the query livedata
     */
    fun recipeSearch(query: String) {
        queryAsLiveData.postValue(query)
    }

    /**
     * get the last query value
     */
    fun lastQueryValue(): String? = queryAsLiveData.value

    override fun onCleared() {
        super.onCleared()
        disposables.value?.clear()
    }
}
