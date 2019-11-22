package com.example.andrewapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andrewapp.Constants.IMAGES_BASE_URL
import com.example.andrewapp.R
import com.example.andrewapp.db.RecipeEntity
import kotlinx.android.synthetic.main.recipe_list_item.view.*

internal class RecipeAdapter(private val mOnItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private var mItems: List<RecipeEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_list_item, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (recipeID, title, readyInMinutes, servings, titleImageUrl, images) = mItems[position]

        holder.titleTV.text = title
        holder.servingsTV.text = servings.toString()
        holder.prepareTimeTV.text = readyInMinutes.toString()
        holder.titleTV.text = title

        Glide.with(holder.itemView.context)
            .load(IMAGES_BASE_URL+titleImageUrl)
            .into(holder.recipeIV)

        holder.itemView.setOnClickListener { v -> mOnItemClickListener.onItemClick(position) }
    }


    override fun getItemCount(): Int {
        return mItems.size
    }

    fun setData(listItems: List<RecipeEntity>) {
        this.mItems = listItems
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeIV = itemView.recipe_list_imageview
        val titleTV = itemView.recipeTitle
        val servingsTV = itemView.servings
        val prepareTimeTV = itemView.timeToPrepare
    }

    companion object {

        private val TAG = RecipeAdapter::class.java.simpleName
    }

}
