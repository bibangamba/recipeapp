package com.example.andrewapp.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.andrewapp.di.multibindingkey.ViewModelKey;
import com.example.andrewapp.viewmodel.RecipeViewModelFactory;
import com.example.andrewapp.viewmodel.viewmodelimpl.AuthViewModelImpl;
import com.example.andrewapp.viewmodel.viewmodelimpl.RecipeViewModelImpl;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(RecipeViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModelImpl.class)
    abstract ViewModel bindsRecipeViewModel(RecipeViewModelImpl recipeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModelImpl.class)
    abstract ViewModel bindsAuthViewModel(AuthViewModelImpl authViewModel);

}
