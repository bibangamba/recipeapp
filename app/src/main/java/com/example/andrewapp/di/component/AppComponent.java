package com.example.andrewapp.di.component;

import android.app.Application;
import com.example.andrewapp.di.module.AppModule;
import com.example.andrewapp.view.MainActivity;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(Application application);

    void inject(MainActivity mainActivity);
}
