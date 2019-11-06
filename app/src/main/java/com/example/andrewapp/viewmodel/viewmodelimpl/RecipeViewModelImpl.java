package com.example.andrewapp.viewmodel.viewmodelimpl;

import androidx.lifecycle.ViewModel;
import com.example.andrewapp.data.Recipe;
import com.example.andrewapp.data.RecipesResponse;
import com.example.andrewapp.room.RecipeRepository;
import com.example.andrewapp.viewmodel.RecipeViewModel;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;

public class RecipeViewModelImpl extends ViewModel implements RecipeViewModel {
    private RecipeRepository mRecipeRepository;

    @Inject
    public RecipeViewModelImpl(RecipeRepository repo) {
        this.mRecipeRepository = repo;
    }

    @Override
    public Single<Long> insert(Recipe recipe) {
        return mRecipeRepository.insert(recipe);
    }

    @Override
    public Single<List<Long>> insertRecipes(List<Recipe> recipes) {
        return mRecipeRepository.insertRecipes(recipes);
    }

    @Override
    public Completable update(Recipe recipe) {
        return mRecipeRepository.update(recipe);
    }

    @Override
    public Single<Integer> delete(Recipe recipe) {
        return mRecipeRepository.delete(recipe);
    }

    @Override
    public Flowable<List<Recipe>> getAllRecipes() {
        return mRecipeRepository.getAllRecipes();
    }

    @Override
    public Single<Integer> deleteAllRecipes() {
        return mRecipeRepository.deleteAllRecipes();
    }

    @Override
    public void initialPopulateLocalDatabase() {
        Consumer<RecipesResponse> getRecipesOnNext = recipesResponse -> {
            List<Recipe> recipes = recipesResponse.getRecipes();
            if (!recipes.isEmpty()) {
                insertRecipes(recipes).subscribe();
            } else {
                //trigger error/info message to UI
                Timber.d("########## retrieving recipes from web API returned an empty list");
            }
        };

        Consumer<Throwable> getRecipesOnError = throwable -> {
            //propagate error to view layer
            Timber.d("########## Error on retrieving recipes from web API");
        };
        mRecipeRepository.retrieveRecipesFromWebService()
                .subscribeOn(Schedulers.io())
                .subscribe(getRecipesOnNext, getRecipesOnError);
    }

}
