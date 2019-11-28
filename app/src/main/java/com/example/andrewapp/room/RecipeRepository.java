package com.example.andrewapp.room;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.andrewapp.model.Recipe;
import com.example.andrewapp.model.RecipesResponse;
import com.example.andrewapp.service.Endpoints;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";
    private static final String INSERT = "insert";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String DELETE_ALL = "delete_all";
    private RecipeDao mRecipeDao;

    public RecipeRepository(Application application) {
        mRecipeDao = RecipeDataBase.getInstance(application).recipeDao();
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
        new Endpoints().recipeAPI().getRecipes().enqueue(new Callback<RecipesResponse>() {
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
