package com.example.andrewapp.db;

import android.app.Application;
import androidx.room.Room;
import com.example.andrewapp.data.RecipeRepository;
import com.example.andrewapp.data.RecipeRepositoryImpl;
import com.example.andrewapp.service.RecipeAPIService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

import static com.example.andrewapp.Constants.RECIPE_DATABASE;
import static com.example.andrewapp.db.Migrations.MIGRATION_1_2;

@Module
public class RoomModule {
    private RecipeDataBase mRecipeDatabase;

    public RoomModule(Application application) {
        this.mRecipeDatabase = Room.databaseBuilder(
                application,
                RecipeDataBase.class,
                RECIPE_DATABASE)
                .addMigrations(MIGRATION_1_2)
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
        return new  RecipeRepositoryImpl(recipeDao, recipeAPIService) {
        };
    }

}
