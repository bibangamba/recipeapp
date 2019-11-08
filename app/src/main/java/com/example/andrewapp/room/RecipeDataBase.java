package com.example.andrewapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.andrewapp.data.RecipeEntity;

@Database(entities = {RecipeEntity.class, RecipeFtsEntity.class}, version = 2, exportSchema = false)
public abstract class RecipeDataBase extends RoomDatabase {

    public abstract RecipeDao recipeDao();
}
