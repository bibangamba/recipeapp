package com.example.andrewapp.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.andrewapp.di.multibindingkey.ViewModelKey;
import com.example.andrewapp.viewmodel.RecipeViewModel;
import com.example.andrewapp.viewmodel.RecipeViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(RecipeViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    abstract ViewModel bindsRecipeViewModel(RecipeViewModel recipeViewModel);

}
