package com.example.andrewapp.room;

import com.example.andrewapp.data.RecipeEntity;
import com.example.andrewapp.data.RecipesResponse;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import java.util.List;

public interface RecipeRepository {
    Completable insert(RecipeEntity recipe);

    Completable insertRecipes(List<RecipeEntity> recipes);

    Completable update(RecipeEntity recipe);

    Completable delete(RecipeEntity recipe);

    Completable deleteAllRecipes();

    Flowable<List<RecipeEntity>> getAllRecipes();

    Observable<List<RecipeEntity>> recipeSearchLocal(String query);

    Flowable<RecipesResponse> recipeSearchNetwork(String query, int results);
}
