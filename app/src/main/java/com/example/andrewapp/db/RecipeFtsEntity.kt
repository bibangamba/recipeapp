package com.example.andrewapp.db

import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "recipe_fts")
@Fts4(contentEntity = RecipeEntity::class)
data class RecipeFtsEntity(var title: String = "")
