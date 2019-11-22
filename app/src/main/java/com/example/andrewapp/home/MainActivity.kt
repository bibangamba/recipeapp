package com.example.andrewapp.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andrewapp.BaseApplication
import com.example.andrewapp.R
import com.example.andrewapp.adapter.RecipeAdapter
import com.example.andrewapp.db.RecipeEntity
import com.example.andrewapp.viewmodel.viewmodelimpl.RecipeViewModelImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), RecipeAdapter.OnItemClickListener {

    @Inject
    lateinit var mRecipeViewModel: RecipeViewModelImpl

    private val adapter = RecipeAdapter(this)
    private var mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as BaseApplication).appComponent.inject(this)
        Timber.d("###### ON CREATE: created new instance of MainActivity ######")

        handleIntent(intent)
        recipes_recyclerview.layoutManager = LinearLayoutManager(this)
        recipes_recyclerview.adapter = adapter
//        val recipesTextView: TextView = findViewById(R.id.recipes_textview)
//        mRecipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModelImpl::class.java)

        mDisposables.add(
            mRecipeViewModel.allRecipes
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { recipes ->
                    Timber.d("###### received update to recipes in the database ######")
//                    recipesTextView.text = recipes.toString()

                    adapter.setData(recipes)
                }
        )

        mRecipeViewModel.delayedInsertRecipeDebug()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        associateSearchConfigToSearchView(menu)
        return true
    }


    override fun onPause() {
        super.onPause()
        mDisposables.dispose()
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEARCH -> {
                val query = intent.getStringExtra(SearchManager.QUERY)!!
                Timber.d("###### SEARCH QUERY: $query ######")
                triggerSearch(query)
            }
        }
    }

    private fun triggerSearch(query: String) {
        Timber.d("###### triggerSearch running. ######")

        val onNext = Consumer<PagedList<RecipeEntity>> { recipes ->
            Timber.d("###### List of recipes %s", recipes)
            adapter.setData(recipes)
        }
        val onError = Consumer<Throwable> { error ->
            Timber.d("###### Experienced error: ")
            Timber.e(error)
            //todo: handle error propagation to view layer
        }

        val onComplete = Action {
            Timber.d("###### Completed local search request ######")
            //todo: handle view layer propagation
        }

        mDisposables.add(
            mRecipeViewModel.recipeSearchLocal(query)
                .subscribe(onNext, onError, onComplete)
        )

    }

    override fun onItemClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun associateSearchConfigToSearchView(menu: Menu) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        Timber.d("###### SEARCH VIEW: $searchView")
    }
}
