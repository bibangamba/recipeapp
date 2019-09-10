package com.example.andrewapp.view

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.andrewapp.BaseApplication
import com.example.andrewapp.R
import com.example.andrewapp.model.Recipe
import com.example.andrewapp.room.RecipeRepository
import com.example.andrewapp.viewmodel.RecipeViewModel
import com.example.andrewapp.viewmodel.RecipeViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repo: RecipeRepository
    @Inject
    lateinit var viewModelFactory: RecipeViewModelFactory

    private lateinit var mRecipeViewModel: RecipeViewModel
    lateinit var mAllRecipes: List<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as BaseApplication).appComponent.inject(this)

        Log.d("MainActivity", repo.toString())

        val recipesTextView: TextView = findViewById(R.id.recipes_textview)
        mRecipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)
        mRecipeViewModel.allRecipes.observe(this, Observer<List<Recipe>> { recipes ->
            Log.d("MainActivity", "recipes ============> $recipes")
            recipesTextView.text = recipes.toString()
        })


    }
}
