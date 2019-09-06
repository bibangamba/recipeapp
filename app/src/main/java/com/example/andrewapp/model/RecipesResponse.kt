package com.example.andrewapp.model

import com.google.gson.annotations.SerializedName

class RecipesResponse {

    @SerializedName("results")
    lateinit var recipes: List<Recipe>

}
