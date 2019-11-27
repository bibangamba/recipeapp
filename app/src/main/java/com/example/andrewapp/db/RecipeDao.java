package com.example.andrewapp.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(RecipeEntity recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<RecipeEntity> recipes);

    @Update
    Completable update(RecipeEntity recipe);

    @Delete
    Completable delete(RecipeEntity recipe);


    @Query("SELECT * FROM recipe_table ORDER BY id")
    Flowable<List<RecipeEntity>> getAllRecipes();

    @Query("DELETE FROM recipe_table")
    Completable deleteAllRecipes();

    @Transaction
    @Query("SELECT * FROM recipe_table WHERE ( title LIKE :query)")
    Observable<List<RecipeEntity>> searchRecipesRx(String query);

    @Transaction
//    @Query("SELECT recipe_table.* FROM recipe_table " +
//            "JOIN recipe_fts ON (recipe_table.id = recipe_fts.rowid) WHERE recipe_fts MATCH :query")
    @Query("SELECT * FROM recipe_table WHERE ( title LIKE :query)")
    DataSource.Factory<Integer, RecipeEntity> searchRecipes(String query);

}
