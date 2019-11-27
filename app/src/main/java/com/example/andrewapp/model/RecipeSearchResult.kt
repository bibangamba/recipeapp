package com.example.andrewapp.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.andrewapp.db.RecipeEntity
import io.reactivex.disposables.CompositeDisposable

/**
 * Class that models the response from a search request
 *
 * @param data - list of recipes returned from the search
 * @param networkErrors - errors that were encountered during the search
 * @param disposables - composite disposable holding all disposables
 *                      generated during database & network rxJava calls
 */
data class RecipeSearchResult(
    val data: LiveData<PagedList<RecipeEntity>>,
    val networkErrors: LiveData<String>,
    val disposables: LiveData<CompositeDisposable>
)
