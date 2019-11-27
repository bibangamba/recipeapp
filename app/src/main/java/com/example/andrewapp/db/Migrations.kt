package com.example.andrewapp.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {

    @JvmField
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE VIRTUAL TABLE IF NOT EXISTS `recipe_fts` USING FTS4(" +
                        "`findRecipe`, content=`recipe_table`)"
            )

            //database.execSQL("INSERT INTO recipe_fts(recipe_fts) VALUES ('rebuild')")
            database.execSQL("INSERT INTO recipe_fts (`rowid`, `findRecipe`) SELECT id, title FROM recipe_table")
        }

    }
}
