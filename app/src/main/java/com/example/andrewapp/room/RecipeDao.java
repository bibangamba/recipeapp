package com.example.andrewapp.room;

import androidx.room.*;
import com.example.andrewapp.data.Recipe;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    Single<Long> insert(Recipe recipe);

    @Insert
    Single<List<Long>> insertAll(List<Recipe> recipes);

    @Update
    Completable update(Recipe recipe);

    @Delete
    Single<Integer> delete(Recipe recipe);


    @Query("SELECT * FROM recipe_table ORDER BY id")
    Flowable<List<Recipe>> getAllRecipes();

    @Query("DELETE FROM recipe_table")
    Single<Integer> deleteAllRecipes();

}
