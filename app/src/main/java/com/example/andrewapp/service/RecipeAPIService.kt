package com.example.andrewapp.service

import com.example.andrewapp.model.RecipesResponse
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET

interface RecipeAPIService {
    companion object {
        const val queryUrl: String =
            "/recipes/search?query=\"burger\"&number=10&apiKey=625aab453d6b41b3b6c63480158674ad"

    }

    @GET(queryUrl)
    fun getRecipes(): Flowable<RecipesResponse>

}
