package com.example.andrewapp

object Constants {
    const val BASE_URL = "https://api.spoonacular.com"
    const val API_KEY = "apiKey"
    const val INSERT = "insert"
    const val UPDATE = "update"
    const val DELETE = "delete"
    const val DELETE_ALL = "delete_all"
    const val RECIPE_DATABASE = "recipe_database"
    const val IMAGES_BASE_URL = "https://spoonacular.com/recipeImages/"
    const val GOOGLE_ID_PROVIDER = "google.com"
    const val FIREBASE_IDP_AUTH_URL =
        "https://identitytoolkit.googleapis.com/v1/accounts:signInWithIdp?key=${BuildConfig.FIREBASE_API_KEY}"
    const val NUM_RECIPE_RESULTS_DEFAULT = 20

}
