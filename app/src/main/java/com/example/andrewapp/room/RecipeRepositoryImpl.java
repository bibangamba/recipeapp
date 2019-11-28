package com.example.andrewapp.room;

import com.example.andrewapp.model.Recipe;
import com.example.andrewapp.model.RecipesResponse;
import com.example.andrewapp.service.RecipeAPIService;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.List;

public class RecipeRepositoryImpl implements RecipeRepository {

    private RecipeDao mRecipeDao;
    private RecipeAPIService mRecipeAPI;

    @Inject
    public RecipeRepositoryImpl(RecipeDao recipeDao, RecipeAPIService recipeAPI) {
        this.mRecipeDao = recipeDao;
        this.mRecipeAPI = recipeAPI;
    }

    @Override
    public Single<Long> insert(Recipe recipe) {
        return mRecipeDao.insert(recipe)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Long>> insertRecipes(List<Recipe> recipes) {
        return mRecipeDao.insertAll(recipes)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable update(Recipe recipe) {
        return mRecipeDao.update(recipe)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> delete(Recipe recipe) {
        return mRecipeDao.delete(recipe)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Integer> deleteAllRecipes() {
        return mRecipeDao.deleteAllRecipes()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<Recipe>>getAllRecipes() {
        return mRecipeDao.getAllRecipes();
    }

    @Override
    public Flowable<RecipesResponse> retrieveRecipesFromWebService() {
        return mRecipeAPI.getRecipes();
    }

}
