package com.example.andrewapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipe_table")
data class Recipe(
    @ColumnInfo(name = "recipe_id")
    var recipeID: Int = 0,

    var title: String = "",

    @ColumnInfo(name = "minutes_to_prepare")
    var readyInMinutes: Int = 0,

    var servings: Int = 0,

    @ColumnInfo(name = "image_url")
    @SerializedName("image")
    var imageName: String = "",

    @Ignore
    @SerializedName("imageUrls")
    var images: List<String> = listOf("")
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun toString(): String {
        return title
    }
}
