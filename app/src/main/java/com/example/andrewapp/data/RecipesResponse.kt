package com.example.andrewapp.data

import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("results")
    val recipes: List<RecipeEntity>
)
