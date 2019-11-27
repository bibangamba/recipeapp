package com.example.andrewapp.data;

import androidx.paging.DataSource;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.example.andrewapp.db.RecipeDao;
import com.example.andrewapp.db.RecipeEntity;
import com.example.andrewapp.service.RecipeAPIService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RecipeRepositoryImpl implements RecipeRepository {

    public static final int PAGE_SIZE = 50;
    private RecipeDao mRecipeDao;
    private RecipeAPIService mRecipeAPI;

    @Inject
    public RecipeRepositoryImpl(RecipeDao recipeDao, RecipeAPIService recipeAPI) {
        this.mRecipeDao = recipeDao;
        this.mRecipeAPI = recipeAPI;
    }

    @Override
    public Completable insert(RecipeEntity recipe) {
        Timber.i("###### DAO instance on single insert: %s", mRecipeDao.hashCode());

        return mRecipeDao.insert(recipe)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insertRecipes(List<RecipeEntity> recipes) {
        Timber.i("###### DAO instance on multi insert: %s", mRecipeDao.hashCode());

        return mRecipeDao.insert(recipes)
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
    public Observable<PagedList<RecipeEntity>> recipeSearchLocal(String query) {

        DataSource.Factory dataSourceFactory = mRecipeDao.searchRecipes(query);
        RecipeBoundaryCallback boundaryCallback = new RecipeBoundaryCallback(this, mRecipeAPI, query);
        Observable<PagedList<RecipeEntity>> observable = new RxPagedListBuilder(dataSourceFactory, PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .buildObservable();

        observable.subscribe((recipes) -> {
            Timber.i("###### recipes from boundary: %s", recipes);
        }, (error) -> {
            Timber.e(error);
        });

        return observable;
    }

    @Override
    public void triggerApiSearch(String query, int results, int offset) {
        Consumer<RecipeSearchResponse> onNext = response -> {
            List<RecipeEntity> recipes = response.getRecipes();
            Timber.d("###### List of recipes %s", recipes);

            if (recipes.isEmpty()) {
                // TODO: 2019-11-10  Set FLAG to prevent recursive network search when no recipes found ######";
            } else {
                saveRecipesToLocal(recipes);
            }
        };
        Consumer<Throwable> onError = (error) -> {
            Timber.d("###### Experienced error: %s", error);
            //todo: handle error propagation to view layer
        };

        Action onComplete = () -> {
            Timber.d("###### Completed network search request ######");
            //todo: handle view layer propagation e.g. stop loader ui
        };
        mRecipeAPI.getRecipes(query, results, offset)
                .subscribeOn(Schedulers.io())
                .subscribe(onNext, onError, onComplete);
    }


    public void triggerLocalSearch(String query) {
        String wildCardQuery = String.format("*%s*", query);
        Consumer<List<RecipeEntity>> onNext = recipes -> {
            Timber.d("###### List of recipes %s", recipes);

//            if (recipes.isEmpty()) {
//                Timber.d("###### Trigger network search since no data locally ######");
//                triggerWebServiceSearch(query);
//            } else {
//                Timber.d("###### Set data for recycler view adapter. ######");
//            }
        };
        Consumer<Throwable> onError = (error) -> {
            Timber.d("###### Experienced error: %s", error);
            //todo: handle error propagation to view layer
        };

        Action onComplete = () -> {
            Timber.d("###### Completed local search request ######");
            //todo: handle view layer propagation
        };

        recipeSearchLocal(wildCardQuery)
                .subscribe(onNext, onError, onComplete);
    }

    @Override
    public void saveRecipesToLocal(List<RecipeEntity> recipes) {
        Action onComplete = () -> Timber.d("###### Saved recipes to database ######");
        Consumer<Throwable> onError = error -> {
            //TODO: what happens here? Do we propagate error to view layer or retry in hiding?
            Timber.d("###### Error while inserting into database");
            Timber.e(error);
        };
        insertRecipes(recipes)
                .subscribe(onComplete, onError);
    }


}
