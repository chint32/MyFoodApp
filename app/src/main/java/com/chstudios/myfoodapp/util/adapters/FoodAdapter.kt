package com.chstudios.myfoodapp.util.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chstudios.myfoodapp.R
import com.chstudios.myfoodapp.data.local.model.Food
import org.w3c.dom.Text
import javax.inject.Inject


class FoodAdapter @Inject constructor(): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    var foods = emptyList<Food>()

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodNameView: TextView = itemView.findViewById(R.id.food_name)
        val foodImageView: ImageView = itemView.findViewById(R.id.food_image)
        val numberOfServs: TextView = itemView.findViewById(R.id.numberOfServings_value)
        val calories: TextView = itemView.findViewById(R.id.cals_value)
        val fat: TextView = itemView.findViewById(R.id.fats_value)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_food_item,
            parent,
            false
        )
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val current = foods[position]
        holder.foodNameView.text = current.title
        holder.numberOfServs.text = current.numOfServings.toString()
        holder.calories.text = current.cals.toString()
        holder.fat.text = current.fats
        val url: String? = current.imgpath


        Glide.with(holder.itemView.context)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.foodImageView)

        val args = Bundle()
        args.putString("food_name", current.title)
        args.putString("image_path", current.imgpath)
        args.putString("description", current.description)
        args.putString("tags", current.tags)
        args.putString("serving_size", current.servSize)
        args.putDouble("number_of_servings", current.numOfServings)
        args.putInt("calories", current.cals)
        args.putString("fat", current.fats.toString())
        args.putString("protein", current.proteen.toString())
        args.putString("carbs", current.carbos.toString())
        args.putDouble("calcium", current.calc)
        args.putDouble("iron", current.fE)

        holder.itemView.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.action_inventoryFragment_to_foodDetailFragment, args)
        }
    }

    internal fun setFoods(foods: List<Food>) {
        this.foods = foods
        notifyDataSetChanged()
    }

    override fun getItemCount() = foods.size
}