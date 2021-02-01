package com.chstudios.myfoodapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.chstudios.myfoodapp.R
import kotlinx.android.synthetic.main.fragment_food_detail.*


class FoodDetailFragment : Fragment(R.layout.fragment_food_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val foodName = requireArguments().getString("food_name")
            val imagePath = requireArguments().getString("image_path")
            val description = requireArguments().getString("description")
            val tags = requireArguments().getString("tags")
            val servingSize = requireArguments().getString("serving_size")
            val numOfSerrvings = requireArguments().getDouble("number_of_servings")
            val calories = requireArguments().getInt("calories")
            val fat = requireArguments().getString("fat")
            val protein = requireArguments().getString("protein")
            val carbs = requireArguments().getString("carbs")
            val calcium = requireArguments().getDouble("calcium")
            val iron = requireArguments().getDouble("iron")


        detail_food_name.text = foodName

        Glide.with(requireContext())
            .load(imagePath)
            .placeholder(R.drawable.ic_launcher_background)
            .into(detail_food_image)

        detail_tags.text = tags
        detail_description.text = description
        serving_size_value.text = servingSize
        num_servings_value.text = numOfSerrvings.toString()
        calories_value.text = calories.toString()
        fat_value.text = fat
        protein_value.text = protein
        carbs_value.text = carbs
        calcium_value.text = calcium.toString()
        iron_value.text = iron.toString()
    }
}