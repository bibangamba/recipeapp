package com.example.andrewapp.room;

import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.andrewapp.model.Recipe;
import com.example.andrewapp.model.RecipesResponse;
import com.example.andrewapp.service.RecipeAPIService;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

import static com.example.andrewapp.Constants.*;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";

    private RecipeDao mRecipeDao;
    private RecipeAPIService mRecipeAPI;

    @Inject
    public RecipeRepository(RecipeDao recipeDao, RecipeAPIService recipeAPI) {
        this.mRecipeDao = recipeDao;
        this.mRecipeAPI = recipeAPI;
    }

    public void insert(Recipe recipe) {
        new DatabaseOperationsAsyncTask(mRecipeDao, INSERT).execute(recipe);
    }

    public void insertRecipes(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            insert(recipe);
        }
    }

    public void update(Recipe recipe) {
        new DatabaseOperationsAsyncTask(mRecipeDao, UPDATE).execute(recipe);
    }

    public void delete(Recipe recipe) {
        new DatabaseOperationsAsyncTask(mRecipeDao, DELETE).execute(recipe);
    }

    public void deleteAllRecipes() {
        new DatabaseOperationsAsyncTask(mRecipeDao, DELETE_ALL).execute();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        mRecipeDao.getAllRecipes().observeForever((recipes -> {
            if (recipes.isEmpty()) {
                Log.d(TAG, "getAllRecipes: ==============> " + "No recipes in the DB, downloading....");
                retrieveRecipesFromWebService();
            }
        }));
        return mRecipeDao.getAllRecipes();
    }

    private void retrieveRecipesFromWebService() {
        mRecipeAPI.getRecipes().enqueue(new Callback<RecipesResponse>() {
            @Override
            public void onResponse(@NotNull Call<RecipesResponse> call, @NotNull Response<RecipesResponse> response) {
                if (response.body() != null) {
                    insertRecipes(response.body().getRecipes());
                }
            }

            @Override
            public void onFailure(@NotNull Call<RecipesResponse> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure ==============> :", t.getCause());
            }
        });
    }

    static class DatabaseOperationsAsyncTask extends AsyncTask<Recipe, Void, Void> {

        private RecipeDao mRecipeDao;
        private String mOperation;

        DatabaseOperationsAsyncTask(RecipeDao recipeDao, String operation) {
            mRecipeDao = recipeDao;
            mOperation = operation;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            switch (mOperation) {
                case INSERT:
                    mRecipeDao.insert(recipes[0]);
                    break;
                case UPDATE:
                    mRecipeDao.delete(recipes[0]);
                    break;
                case DELETE:
                    mRecipeDao.delete(recipes[0]);
                    break;
                case DELETE_ALL:
                    mRecipeDao.deleteAllRecipes();
                    break;
                default:
                    Log.d("DatabaseOperationsTask", "Operation not supported");
                    break;
            }
            return null;
        }
    }
}
