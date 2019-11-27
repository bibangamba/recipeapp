package com.example.andrewapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * This class represents a findRecipe object
 *
 */
@Entity(tableName = "recipe_table"/*, indices = [Index(value = ["recipe_id"], unique = true)]*/)
data class RecipeEntity(
    var id: Int = 0,

    var title: String = "",

    @ColumnInfo(name = "minutes_to_prepare")
    var readyInMinutes: Int = 0,

    var servings: Int = 0,

    @ColumnInfo(name = "image_url")
    @SerializedName("image")
    var titleImageUrl: String = "",

    @Ignore
    @SerializedName("imageUrls")
    var images: List<String> = listOf("")
) {
    @PrimaryKey(autoGenerate = true)
    var recipe_id: Int = 0

    override fun toString(): String {
        return title
    }
}
