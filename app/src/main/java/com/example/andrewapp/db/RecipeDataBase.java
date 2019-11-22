package com.example.andrewapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecipeEntity.class, RecipeFtsEntity.class}, version = 2, exportSchema = false)
public abstract class RecipeDataBase extends RoomDatabase {

    public abstract RecipeDao recipeDao();
}
