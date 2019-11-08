package com.example.andrewapp.di.module;

import com.example.andrewapp.BuildConfig;
import com.example.andrewapp.service.IdentityProviderAuthService;
import com.example.andrewapp.service.RecipeAPIService;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

import static com.example.andrewapp.Constants.API_KEY;

@Module
public class WebServiceModule {
    private String mBaseUrl;

    public WebServiceModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        addHttpLoggingInterceptor(clientBuilder);

        addApiKey(clientBuilder);

        return clientBuilder.build();
    }

    private void addApiKey(OkHttpClient.Builder clientBuilder) {
        clientBuilder.addInterceptor(chain -> {
            Request requestWithApiKey = addApiKeyToRequest(chain);
            return chain.proceed(requestWithApiKey);
        });
    }

    private void addHttpLoggingInterceptor(OkHttpClient.Builder clientBuilder) {
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(
                    new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY));
        }
    }

    @NotNull
    private Request addApiKeyToRequest(Interceptor.Chain chain) {
        Request originalRequest = chain.request();

        HttpUrl urlWithApiKey = originalRequest.url()
                .newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.RECIPE_API_KEY)
                .build();

        return originalRequest.newBuilder()
                .url(urlWithApiKey)
                .build();
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
