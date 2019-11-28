package com.example.andrewapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.andrewapp.model.Recipe;
import com.example.andrewapp.room.RecipeRepository;

import javax.inject.Inject;
import java.util.List;

public class RecipeViewModel extends ViewModel {
    RecipeRepository mRecipeRepository;

    @Inject
    public RecipeViewModel(RecipeRepository repo) {
        this.mRecipeRepository = repo;
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
