package com.example.andrewapp.viewmodel;

import com.example.andrewapp.model.Recipe;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

interface RecipeViewModelBase {
    Single<Long> insert(Recipe recipe);

    Single<List<Long>> insertRecipes(List<Recipe> recipes);

    Completable update(Recipe recipe);

    Single<Integer> delete(Recipe recipe);

    Flowable<List<Recipe>> getAllRecipes();

    Single<Integer> deleteAllRecipes();

    void initialPopulateLocalDatabase();
}
