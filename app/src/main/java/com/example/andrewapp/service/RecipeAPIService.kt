package com.example.andrewapp.service

import com.example.andrewapp.data.RecipesResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeAPIService {

    @GET("/recipes/search")
    fun getRecipes(
        @Query("query") query: String,
        @Query("number") numberOfResults: Int
    ): Flowable<RecipesResponse>

}
