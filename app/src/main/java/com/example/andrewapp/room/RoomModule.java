package com.example.andrewapp.room;

import android.app.Application;
import androidx.room.Room;
import com.example.andrewapp.service.RecipeAPIService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

import static com.example.andrewapp.Constants.RECIPE_DATABASE;

@Module
public class RoomModule {
    private RecipeDataBase mRecipeDatabase;

    public RoomModule(Application application) {
        this.mRecipeDatabase = Room.databaseBuilder(
                application,
                RecipeDataBase.class,
                RECIPE_DATABASE)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    RecipeDataBase providesRecipeDatabase() {
        return mRecipeDatabase;
    }

    @Provides
    @Singleton
    RecipeDao providesRecipeDao() {
        return mRecipeDatabase.recipeDao();
    }

    @Provides
    @Singleton
    RecipeRepository providesRecipeRepository(RecipeDao recipeDao, RecipeAPIService recipeAPIService) {
        return new RecipeRepository(recipeDao, recipeAPIService);
    }

}
