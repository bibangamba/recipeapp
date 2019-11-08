package com.example.andrewapp.room;

import com.example.andrewapp.data.RecipeEntity;
import com.example.andrewapp.data.RecipesResponse;
import com.example.andrewapp.service.RecipeAPIService;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
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
    public Completable insert(RecipeEntity recipe) {
        return mRecipeDao.insert(recipe)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insertRecipes(List<RecipeEntity> recipes) {
        return mRecipeDao.insertAll(recipes)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable update(RecipeEntity recipe) {
        return mRecipeDao.update(recipe)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable delete(RecipeEntity recipe) {
        return mRecipeDao.delete(recipe)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteAllRecipes() {
        return mRecipeDao.deleteAllRecipes()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<RecipeEntity>> getAllRecipes() {
        return mRecipeDao.getAllRecipes();
    }

    @Override
    public Observable<List<RecipeEntity>> recipeSearchLocal(String query) {
        return mRecipeDao.searchRecipes(query).subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<RecipesResponse> recipeSearchNetwork(String query, int results) {
        return mRecipeAPI.getRecipes(query, results)
                .subscribeOn(Schedulers.io());
    }

}
