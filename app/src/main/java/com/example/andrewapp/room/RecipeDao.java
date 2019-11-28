package com.example.andrewapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.andrewapp.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insert(Recipe recipe);

    @Insert
    void update(Recipe recipe);

    @Insert
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipe_table ORDER BY id")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("DELETE FROM recipe_table")
    void deleteAllRecipes();

}
