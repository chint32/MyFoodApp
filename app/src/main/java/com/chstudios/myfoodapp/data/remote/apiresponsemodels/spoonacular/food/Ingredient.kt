package com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food


import com.google.gson.annotations.SerializedName

data class Ingredient(
    val description: Any,
    val name: String,
    @SerializedName("safety_level")
    val safetyLevel: Any
)