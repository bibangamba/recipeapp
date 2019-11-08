package com.example.andrewapp.room

import androidx.room.Entity
import androidx.room.Fts4
import com.example.andrewapp.data.RecipeEntity

@Entity(tableName = "recipe_fts")
@Fts4(contentEntity = RecipeEntity::class)
data class RecipeFtsEntity(var title: String = "")
