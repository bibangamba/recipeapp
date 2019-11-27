package com.example.andrewapp.recipesearch.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andrewapp.BaseApplication
import com.example.andrewapp.R
import com.example.andrewapp.adapter.RecipeAdapterLD
import com.example.andrewapp.db.RecipeEntity
import com.example.andrewapp.recipesearch.viewmodel.RecipeViewModelLD
import com.example.andrewapp.viewmodel.RecipeViewModelFactory
import com.example.andrewapp.viewmodel.viewmodelimpl.RecipeViewModelImpl
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), RecipeAdapterLD.OnItemClickListener {

    @Inject
    lateinit var mViewModelFactory: RecipeViewModelFactory

    @Inject
    lateinit var mRecipeViewModel: RecipeViewModelImpl

    lateinit var mRecipeViewModelLD: RecipeViewModelLD

    private val adapterLD = RecipeAdapterLD(this)
    private var mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("###### ON CREATE: created new instance of SearchActivity ######")
        setContentView(R.layout.activity_main)
        (application as BaseApplication).appComponent.inject(this)
        mRecipeViewModelLD = ViewModelProviders.of(this, mViewModelFactory)
            .get(RecipeViewModelLD::class.java)

        handleIntent(intent)

        initAdapter()

        restoreLastSearch(savedInstanceState)
    }

    private fun restoreLastSearch(savedInstanceState: Bundle?) {
        val query = savedInstanceState?.getString(LAST_QUERY_SEARCH) ?: BLANK

        if (query.isNotBlank()) {
            mRecipeViewModelLD.recipeSearch(query)
        }
    }

    private fun initAdapter() {
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recipes_recyclerview.addItemDecoration(decoration)
        recipes_recyclerview.layoutManager = LinearLayoutManager(this)
        recipes_recyclerview.adapter = adapterLD

        mRecipeViewModelLD.recipes.observe(this, Observer<PagedList<RecipeEntity>> {
            Timber.i("### paged list size: %s", it.size)
            showEmptyList(it?.size == 0)
            adapterLD.submitList(it)
        })

        mRecipeViewModelLD.networkErrors.observe(this, Observer<String> {
            Timber.e("#### network error: $it")
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })

        mRecipeViewModelLD.disposables.observe(this, Observer<CompositeDisposable> {
            Timber.i("#### disposables: $it")
            Timber.i("#### disposables size: ${it.size()}")
            mDisposables.addAll(it)
        })
    }

    private fun showEmptyList(isEmpty: Boolean) {
        if (isEmpty) {
            emptyListTV.visibility = View.VISIBLE
            recipes_recyclerview.visibility = View.GONE
        } else {
            recipes_recyclerview.visibility = View.VISIBLE
            emptyListTV.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        associateSearchConfigToSearchView(menu)
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        Timber.i("### disposables size: ${mDisposables.size()}")
        Timber.i("### disposables: $mDisposables")
        mDisposables.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(LAST_QUERY_SEARCH, mRecipeViewModelLD.lastQueryValue())
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEARCH -> {
                val query = intent.getStringExtra(SearchManager.QUERY)!!
                mRecipeViewModelLD.recipeSearch(query)
            }
        }
    }

    override fun onItemClick(position: Int) {
        Timber.i("#### clicked position $position. ")
        finish()
    }


    private fun associateSearchConfigToSearchView(menu: Menu) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val lastQuery = mRecipeViewModelLD.lastQueryValue()

        if (!lastQuery.isNullOrBlank()) {
            searchView.setQuery(mRecipeViewModelLD.lastQueryValue(), false)
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
    }

    companion object {
        const val LAST_QUERY_SEARCH = "last_query_search"
        const val BLANK = ""
    }
}
