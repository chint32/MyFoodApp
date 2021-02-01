package com.chstudios.myfoodapp.util.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chstudios.myfoodapp.R
import com.chstudios.myfoodapp.data.local.model.Recipe
import javax.inject.Inject

class RecipeAdapter @Inject constructor(): RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    var recipes = emptyList<Recipe>()

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeNameView: TextView = itemView.findViewById(R.id.recipe_name)
        val recipeImageView: ImageView = itemView.findViewById(R.id.recipe_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_list_recipe_item,
            parent,
            false
        )
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val current = recipes[position]
        holder.recipeNameView.text = current.recipeName
        val url: String? = current.imagePath


        Glide.with(holder.itemView.context)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.recipeImageView)
    }

    internal fun setListNames(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    override fun getItemCount() = recipes.size
}