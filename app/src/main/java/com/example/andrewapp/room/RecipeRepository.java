package com.example.andrewapp.room;

import com.example.andrewapp.model.Recipe;
import com.example.andrewapp.model.RecipesResponse;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

public interface RecipeRepository {
    Single<Long> insert(Recipe recipe);

    Single<List<Long>> insertRecipes(List<Recipe> recipes);

    Completable update(Recipe recipe);

    Single<Integer> delete(Recipe recipe);

    Single<Integer> deleteAllRecipes();

    Flowable<List<Recipe>> getAllRecipes();

    Flowable<RecipesResponse> retrieveRecipesFromWebService();
}
