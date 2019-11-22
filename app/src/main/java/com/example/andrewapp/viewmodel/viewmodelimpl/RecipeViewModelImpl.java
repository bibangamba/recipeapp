package com.example.andrewapp.viewmodel.viewmodelimpl;

import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.andrewapp.data.RecipeRepository;
import com.example.andrewapp.db.RecipeEntity;
import com.example.andrewapp.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RecipeViewModelImpl extends ViewModel implements RecipeViewModel {
    private RecipeRepository mRecipeRepository;
    private CompositeDisposable mDisposables = new CompositeDisposable();


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
    public Observable<PagedList<RecipeEntity>> recipeSearchLocal(String query) {
        Timber.d("###### Searching for %s ######", query);
        String wildCardQuery = String.format("*%s*", query);
        return mRecipeRepository.recipeSearchLocal(wildCardQuery)
                .subscribeOn(Schedulers.io());
    }

    private void saveRecipesToLocal(List<RecipeEntity> recipes) {
        Action onComplete = () -> Timber.d("###### Saved recipes to database ######");
        Consumer<Throwable> onError = error -> {
            //TODO: what happens here? Do we propagate error to view layer or retry in hiding?
            Timber.d("###### Error while inserting into database");
            Timber.e(error);
        };
        mDisposables.add(
                insertRecipes(recipes)
                        .subscribe(onComplete, onError)
        );
    }

    public void delayedInsertRecipeDebug() {
        Timber.i("###### Database instance: %s", mRecipeRepository.hashCode());

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Timber.d("###### saving grass hopper pizza to data base ######");
                        RecipeEntity recipe = new RecipeEntity(12, "Grasshopper pizza",
                                30, 4,
                                "teriyaki-chicken-808940.jpg", new ArrayList<>());
                        insert(recipe).subscribe();
                    }
                },
                8000
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposables.clear();
    }
}
