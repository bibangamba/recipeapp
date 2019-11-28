package com.example.andrewapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.andrewapp.model.Recipe;
import com.example.andrewapp.room.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository mRecipeRepository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        mRecipeRepository = new RecipeRepository(application);
    }

    public void insert(Recipe recipe) {
        mRecipeRepository.insert(recipe);
    }

    public void insertRecipes(List<Recipe> recipes) {
        mRecipeRepository.insertRecipes(recipes);
    }

    public void update(Recipe recipe) {
        mRecipeRepository.update(recipe);
    }

    public void delete(Recipe recipe) {
        mRecipeRepository.delete(recipe);
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mRecipeRepository.getAllRecipes();
    }

    public void deleteAllRecipes() {
        mRecipeRepository.deleteAllRecipes();
    }

}
