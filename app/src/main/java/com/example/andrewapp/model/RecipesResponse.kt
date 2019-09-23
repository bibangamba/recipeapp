package com.example.andrewapp.model

import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("results")
    val recipes: List<Recipe>
)
