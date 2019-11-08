package com.example.andrewapp.viewmodel.viewmodelimpl;

import androidx.lifecycle.ViewModel;
import com.example.andrewapp.data.RecipeEntity;
import com.example.andrewapp.data.RecipesResponse;
import com.example.andrewapp.room.RecipeRepository;
import com.example.andrewapp.viewmodel.RecipeViewModel;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

import javax.inject.Inject;
import java.util.List;

public class RecipeViewModelImpl extends ViewModel implements RecipeViewModel {
    private RecipeRepository mRecipeRepository;

    @Inject
    public RecipeViewModelImpl(RecipeRepository repo) {
        this.mRecipeRepository = repo;
    }

    @Override
    public Completable insert(RecipeEntity recipe) {
        return mRecipeRepository.insert(recipe);
    }

    @Override
    public Completable insertRecipes(List<RecipeEntity> recipes) {
        return mRecipeRepository.insertRecipes(recipes);
    }

    @Override
    public Completable update(RecipeEntity recipe) {
        return mRecipeRepository.update(recipe);
    }

    @Override
    public Completable delete(RecipeEntity recipe) {
        return mRecipeRepository.delete(recipe);
    }

    @Override
    public Flowable<List<RecipeEntity>> getAllRecipes() {
        return mRecipeRepository.getAllRecipes();
    }

    @Override
    public Completable deleteAllRecipes() {
        return mRecipeRepository.deleteAllRecipes();
    }

    @Override
    public Observable<List<RecipeEntity>> recipeSearchLocal(String query) {
        return mRecipeRepository.recipeSearchLocal(query);
    }

    @Override
    public Flowable<RecipesResponse> recipeSearchNetwork(String query, int numResults) {
        return mRecipeRepository.recipeSearchNetwork(query, numResults);
    }

//    @Override
//    public void initialPopulateLocalDatabase() {
//        Consumer<RecipesResponse> getRecipesOnNext = recipesResponse -> {
//            List<RecipeEntity> recipes = recipesResponse.getRecipes();
//            if (!recipes.isEmpty()) {
//                insertRecipes(recipes).subscribe();
//            } else {
//                //trigger error/info message to UI
//                Timber.d("########## retrieving recipes from web API returned an empty list");
//            }
//        };
//
//        Consumer<Throwable> getRecipesOnError = throwable -> {
//            //propagate error to view layer
//            Timber.d("########## Error on retrieving recipes from web API");
//        };
//        mRecipeRepository.recipeSearchNetwork()
//                .subscribeOn(Schedulers.io())
//                .subscribe(getRecipesOnNext, getRecipesOnError);
//    }

}
