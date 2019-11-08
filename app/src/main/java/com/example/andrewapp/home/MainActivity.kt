package com.example.andrewapp.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.andrewapp.BaseApplication
import com.example.andrewapp.R
import com.example.andrewapp.data.RecipeEntity
import com.example.andrewapp.data.RecipesResponse
import com.example.andrewapp.viewmodel.RecipeViewModelFactory
import com.example.andrewapp.viewmodel.viewmodelimpl.RecipeViewModelImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: RecipeViewModelFactory

    @Inject
    lateinit var mRecipeViewModel: RecipeViewModelImpl

    private lateinit var mRecipes: Disposable
    private var mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as BaseApplication).appComponent.inject(this)
        Timber.d("###### ON CREATE: created new instance of MainActivity ######")

        handleIntent(intent)

        val recipesTextView: TextView = findViewById(R.id.recipes_textview)
//        mRecipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModelImpl::class.java)

        mRecipes = mRecipeViewModel.allRecipes
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { recipes ->
                Timber.d("###### received update to recipes in the database ######")
                recipesTextView.text = recipes.toString()
            }
//
        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    Timber.d("###### saving grass hopper pizza to data base ######")
                    val recipe = RecipeEntity(
                        12, "Grasshopper pizza",
                        30, 4, "imageUrl"
                    )
                    mRecipeViewModel.insert(recipe)
                        .subscribe()
                }
            }, 10000
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        associateSearchConfigToSearchView(menu)
        return true
    }


    override fun onPause() {
        super.onPause()
        mRecipes.dispose()
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEARCH -> {
                val query = intent.getStringExtra(SearchManager.QUERY)!!
                Timber.d("###### SEARCH QUERY: $query")
                triggerSearch(query)
            }
        }
    }

    private fun triggerSearch(query: String) {
        val onNext = Consumer<List<RecipeEntity>> {
            Timber.d("###### List of recipes $it")
            if (it.isEmpty()) {
                triggerWebServiceSearch(query)
            } else {
                //send list to recycler view adapter
                Timber.d("###### Set data for recycler view adapter. ######")
            }

        }

        val onError = Consumer<Throwable> {
            Timber.e("###### $it")
        }

        val onComplete = Action {
            Timber.i("###### COMPLETED SEARCH QUERY OBSERVATION ######")
        }

        val disposable = mRecipeViewModel.recipeSearchLocal("*$query*")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError, onComplete)

        mDisposables.add(disposable)
    }

    private fun triggerWebServiceSearch(query: String) {
        val onNext = Consumer<RecipesResponse> {
            Timber.i("###### web service search onNext: insert recipes below into local db ######")

            Timber.i("###### web service search onNext: ${it.recipes} ######")

            saveSearchResultsToLocal(it.recipes)
        }
        val onError = Consumer<Throwable> {
            Timber.e("###### web service search error: $it ######")
        }
        val onComplete = Action {
            Timber.i("###### stop loading UI ######")
            Timber.i(
                "###### web service search completed, probably set some flag so we don't keep asking the server for data if search returns zero results ######"
            )
        }

        val disposable = mRecipeViewModel.recipeSearchNetwork(query, 20)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, onError, onComplete)
        mDisposables.add(disposable)
        Timber.d("###### Trigger web service search at this point. ######")
    }

    private fun saveSearchResultsToLocal(recipes: List<RecipeEntity>) {
        val onComplete = {
            Timber.i("###### save to local db completed. ######")
        }

        val onError = { error: Throwable ->
            Timber.i("###### error occurred during save to db:  $error ######")
        }
        val disposable = mRecipeViewModel.insertRecipes(recipes)
//            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)

        mDisposables.add(disposable)
    }

    private fun associateSearchConfigToSearchView(menu: Menu) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        Timber.d("###### SEARCH VIEW: $searchView")
    }
}
