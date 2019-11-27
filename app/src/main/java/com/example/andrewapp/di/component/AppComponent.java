package com.example.andrewapp.di.component;

import android.app.Application;
import com.example.andrewapp.authentication.GoogleSignInActivity;
import com.example.andrewapp.di.module.AppModule;
import com.example.andrewapp.recipesearch.ui.SearchActivity;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(Application application);

    void inject(SearchActivity searchActivity);

    void inject(GoogleSignInActivity googleSignInActivity);
}
