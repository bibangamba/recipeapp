package com.example.andrewapp.data;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import com.example.andrewapp.db.RecipeEntity;
import com.example.andrewapp.service.RecipeAPIService;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import java.util.List;

public class RecipeBoundaryCallback extends PagedList.BoundaryCallback<RecipeEntity> {

    private static final int INITIAL_NETWORK_RESULTS_SIZE = 10;
    private static final int NETWORK_RESULTS_SIZE = 50;
    private boolean isRequestInProgress = false;
    private RecipeRepository mRepo;
    private RecipeAPIService mApi;
    private String mQuery;
    private int mOffset = 0;
    private int mTotalSearchResultsAvailable = 0;

    RecipeBoundaryCallback(RecipeRepository repository, RecipeAPIService api, String query) {
        mRepo = repository;
        mApi = api;
        mQuery = query;
    }

    /**
     * Triggered when the database returns 0 items on initial load.
     * We should then query the API for more items.
     */
    @Override
    public void onZeroItemsLoaded() {
        super.onZeroItemsLoaded();
        requestAndSaveData(mQuery);
    }

    /**
     * Triggered when all the items in the database has been loaded.
     * So we query the API to replenish the database.
     *
     * @param itemAtEnd the last recipe entity that was loaded from the database
     */
    @Override
    public void onItemAtEndLoaded(@NonNull RecipeEntity itemAtEnd) {
        super.onItemAtEndLoaded(itemAtEnd);
        requestAndSaveData(mQuery);
    }

    private void requestAndSaveData(String query) {
        if (isRequestInProgress /*|| mTotalSearchResultsAvailable <= 0*/) {
            return;
        }

        isRequestInProgress = true;

        if (mOffset == 0) {
            triggerApiSearch(mQuery, INITIAL_NETWORK_RESULTS_SIZE, mOffset);
        } else {
            triggerApiSearch(query, NETWORK_RESULTS_SIZE, mOffset);
        }


    }

    private void triggerApiSearch(String query, int results, int offset) {
        Consumer<RecipeSearchResponse> onNext = response -> {
            List<RecipeEntity> recipes = response.getRecipes();

            /**
             * When mOffset is zero and results is equal to initial network results size,
             * we receive the total number of recipes
             */
            if (offset == 0 && results == INITIAL_NETWORK_RESULTS_SIZE) {
                mTotalSearchResultsAvailable = response.getTotalResults();
            }

            mOffset += results;
            mTotalSearchResultsAvailable -= results;

            Timber.d("###### List of recipes %s", recipes);

            if (recipes.isEmpty()) {
                // No recipes found
                Timber.d("###### No recipes found");
            } else {
                mRepo.saveRecipesToLocal(recipes);
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

        //TODO how can I clean up this disposable when the boundary call has no lifecycle awareness
        mApi.getRecipes(query, results, offset)
                .subscribeOn(Schedulers.io())
                .subscribe(onNext, onError, onComplete);
    }
}
