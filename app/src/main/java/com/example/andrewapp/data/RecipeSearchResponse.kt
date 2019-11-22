package com.example.andrewapp.data

import com.example.andrewapp.db.RecipeEntity
import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse(
    @SerializedName("results")
    val recipes: List<RecipeEntity>,
    val totalResults: Int
)
