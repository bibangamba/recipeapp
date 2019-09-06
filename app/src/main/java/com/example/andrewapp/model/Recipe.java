package com.example.andrewapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

import java.util.List;

// TODO: 2019-09-06 Figure out how to separate retrofit model from room mode. maybe an interface or parent class?
@Entity(tableName = "recipe_table")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "recipe_id")
    private int recipeID;

    private String title;

    @ColumnInfo(name = "minutes_to_prepare")
    private int readyInMinutes;

    private int servings;

    @ColumnInfo(name = "image_url")
    @SerializedName("image")
    private String imageName;

    @Ignore
    @SerializedName("imageUrls")
    private List<String> images;

    public Recipe(int recipeID, String title, int readyInMinutes, int servings, String imageName) {
        this.recipeID = recipeID;
        this.title = title;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getTitle() {
        return title;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public String getImageName() {
        return imageName;
    }

    @Override
    public String toString() {
        return title;
    }
}
