package com.example.andrewapp;

import android.app.Application;
import com.example.andrewapp.di.component.AppComponent;
import com.example.andrewapp.di.component.DaggerAppComponent;
import com.example.andrewapp.di.module.AppModule;
import com.example.andrewapp.di.module.WebServiceModule;
import com.example.andrewapp.room.RoomModule;
import timber.log.Timber;

import static com.example.andrewapp.Constants.BASE_URL;

public class BaseApplication extends Application {
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //create dagger component here & inject application into it.

        initDaggerAppComponent();

        initTimberLogger();
    }

    private void initTimberLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initDaggerAppComponent() {
        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .webServiceModule(new WebServiceModule(BASE_URL))
                .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
