package com.example.andrewapp.view

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.andrewapp.R
import com.example.andrewapp.model.Recipe
import com.example.andrewapp.viewmodel.RecipeViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mRecipeViewModel: RecipeViewModel
    lateinit var mAllRecipes: List<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recipesTextView: TextView = findViewById(R.id.recipes_textview)
        mRecipeViewModel = ViewModelProviders.of(this)[(RecipeViewModel::class.java)]
        mRecipeViewModel.allRecipes.observe(this, Observer<List<Recipe>> { recipes ->
            Log.d("MainActivity", "recipes ============> $recipes")
            recipesTextView.text = recipes.toString()
        })

    }
}
