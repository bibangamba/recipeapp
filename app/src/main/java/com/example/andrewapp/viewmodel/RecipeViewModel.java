package com.example.andrewapp.viewmodel;

import com.example.andrewapp.db.RecipeEntity;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import java.util.List;

public interface RecipeViewModel {
    Completable insert(RecipeEntity recipe);

    Completable insertRecipes(List<RecipeEntity> recipes);

    Completable update(RecipeEntity recipe);

    Completable delete(RecipeEntity recipe);

    Flowable<List<RecipeEntity>> getAllRecipes();

    Completable deleteAllRecipes();

    Observable recipeSearchLocal(String query);

}
