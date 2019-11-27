package com.example.andrewapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andrewapp.Constants
import com.example.andrewapp.R
import com.example.andrewapp.db.RecipeEntity

class RecipeAdapterLD(private val itemClickListener: OnItemClickListener) :
    PagedListAdapter<RecipeEntity, RecyclerView.ViewHolder>(RECIPE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipeViewHolderLD.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recipeItem = getItem(position)

        if (recipeItem != null) {
            (holder as RecipeViewHolderLD).bind(recipeItem)
            holder.itemView.setOnClickListener { itemClickListener.onItemClick(position) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {
        private val RECIPE_COMPARATOR = object : DiffUtil.ItemCallback<RecipeEntity>() {
            override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean =
                oldItem == newItem
        }
    }
}


class RecipeViewHolderLD(view: View) : RecyclerView.ViewHolder(view) {
    private val recipeImage: ImageView = view.findViewById(R.id.recipe_list_imageview)
    private val recipeTitle: TextView = view.findViewById(R.id.recipeTitle)
    private val servings: TextView = view.findViewById(R.id.servings)
    private val timeToPrepare: TextView = view.findViewById(R.id.time_to_prepare)
    private val resources = itemView.resources
    private val context = itemView.context

    private var recipe: RecipeEntity? = null

    companion object {
        fun create(parent: ViewGroup): RecipeViewHolderLD {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recipe_list_item, parent, false)
            return RecipeViewHolderLD(view)
        }
    }

    fun bind(recipe: RecipeEntity) {
        this.recipe = recipe

        Glide.with(context)
            .load(Constants.IMAGES_BASE_URL + recipe.titleImageUrl)
            .placeholder(R.drawable.recipe_small)
            .error(R.drawable.recipe_small)
            .into(recipeImage)

        recipeTitle.text = recipe.title
        if (recipe.readyInMinutes == 1) {
            timeToPrepare.text =
                resources.getString(R.string.ready_in_one_minute_text, recipe.readyInMinutes)
        } else {
            timeToPrepare.text =
                resources.getString(R.string.ready_in_multiple_minutes_text, recipe.readyInMinutes)
        }
        servings.text = resources.getString(R.string.feeds_text, recipe.servings)
    }
}
