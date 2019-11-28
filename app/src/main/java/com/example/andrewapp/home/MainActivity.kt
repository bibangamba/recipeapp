package com.example.andrewapp.home

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.andrewapp.BaseApplication
import com.example.andrewapp.R
import com.example.andrewapp.data.Recipe
import com.example.andrewapp.viewmodel.viewmodelimpl.RecipeViewModelImpl
import com.example.andrewapp.viewmodel.RecipeViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: RecipeViewModelFactory

    private lateinit var mRecipeViewModel: RecipeViewModelImpl
    lateinit var mRecipes: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as BaseApplication).appComponent.inject(this)

        val recipesTextView: TextView = findViewById(R.id.recipes_textview)
        mRecipeViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModelImpl::class.java)

        mRecipes = mRecipeViewModel.allRecipes
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { recipes ->
                if (recipes.isEmpty()) {
                    mRecipeViewModel.initialPopulateLocalDatabase()
                }
                recipesTextView.text = recipes.toString()
            }

        Timer().schedule(
            object : TimerTask() {
                override fun run() {
                    val recipe = Recipe(
                        12, "Grasshopper pizza",
                        30, 4, "imageUrl"
                    )
                    mRecipeViewModel.insert(recipe)
                        .subscribe()
                }
            }, 5000
        )

    }

    override fun onPause() {
        super.onPause()
        mRecipes.dispose()
    }
}
