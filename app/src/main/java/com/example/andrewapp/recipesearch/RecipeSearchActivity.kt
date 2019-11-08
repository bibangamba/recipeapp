package com.example.andrewapp.recipesearch

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.andrewapp.R
import timber.log.Timber

class RecipeSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_search)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEARCH -> {
                val searchQuery = intent.getStringExtra(SearchManager.QUERY)
                Timber.d("###### SEARCH QUERY: $searchQuery")
                //viewmodel.doTheSearch(searchQuery)
            }
        }
    }

}
