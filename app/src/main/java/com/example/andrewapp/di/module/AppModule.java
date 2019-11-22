package com.example.andrewapp.di.module;

import android.app.Application;
import android.content.Context;
import com.example.andrewapp.db.RoomModule;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(includes = {WebServiceModule.class, RoomModule.class, ViewModelModule.class})
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Context providesApplicationContext() {
        return mApplication;
    }
}
