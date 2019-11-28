package com.example.andrewapp.di.module;

import com.example.andrewapp.BuildConfig;
import com.example.andrewapp.service.IdentityProviderAuthService;
import com.example.andrewapp.service.RecipeAPIService;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

@Module
public class WebServiceModule {
    private String mBaseUrl;

    public WebServiceModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {
        HttpLoggingInterceptor.Level loggingLevel;

        if (BuildConfig.DEBUG) {
            loggingLevel = HttpLoggingInterceptor.Level.BASIC;
        } else {
            loggingLevel = HttpLoggingInterceptor.Level.NONE;
        }
        return new OkHttpClient.Builder().addInterceptor(
                new HttpLoggingInterceptor().setLevel(loggingLevel)).build();
    }

    @Provides
    Retrofit providesRetrofitInstance(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    RecipeAPIService providesRecipeAPIService(Retrofit retrofit) {
        return retrofit.create(RecipeAPIService.class);
    }

    @Provides
    IdentityProviderAuthService providesGoogleAuthServiceService(Retrofit retrofit) {
        return retrofit.create(IdentityProviderAuthService.class);
    }
}
