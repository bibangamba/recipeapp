package com.example.andrewapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.andrewapp.model.Recipe;

@Database(entities = {Recipe.class}, version = 1)
public abstract class RecipeDataBase extends RoomDatabase {

    public abstract RecipeDao recipeDao();
}
