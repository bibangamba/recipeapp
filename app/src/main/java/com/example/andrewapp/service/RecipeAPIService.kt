package com.example.andrewapp.service

import com.example.andrewapp.data.RecipeSearchResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeAPIService {

    @GET("/recipes/search")
    fun getRecipes(
        @Query("query") query: String,
        @Query("number") numberOfResults: Int,
        @Query("offset") offset: Int
    ): Flowable<RecipeSearchResponse>

}
