package com.example.andrewapp.data;

import com.example.andrewapp.db.RecipeEntity;
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

    Observable recipeSearchLocal(String query);

    void triggerApiSearch(String query, int results, int offset);

    void saveRecipesToLocal(List<RecipeEntity> recipes);
}
