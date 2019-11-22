package com.example.andrewapp.db;

import androidx.paging.DataSource;
import androidx.room.*;
import io.reactivex.Completable;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    Completable insert(RecipeEntity recipe);

    @Insert
    Completable insertAll(List<RecipeEntity> recipes);

    @Update
    Completable update(RecipeEntity recipe);

    @Delete
    Completable delete(RecipeEntity recipe);


    @Query("SELECT * FROM recipe_table ORDER BY id")
    Flowable<List<RecipeEntity>> getAllRecipes();

    @Query("DELETE FROM recipe_table")
    Completable deleteAllRecipes();

//    @Transaction
//    @Query("SELECT recipe_table.* FROM recipe_table " +
//            "JOIN recipe_fts ON (recipe_table.id = recipe_fts.rowid) WHERE recipe_fts MATCH :query")
//    Observable<PagedList<RecipeEntity>> searchRecipes(String query);

    @Transaction
    @Query("SELECT recipe_table.* FROM recipe_table " +
            "JOIN recipe_fts ON (recipe_table.id = recipe_fts.rowid) WHERE recipe_fts MATCH :query")
    DataSource.Factory<Integer, RecipeEntity> searchRecipes(String query);

}
