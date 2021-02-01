package com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food


data class Nutrition(
    val caloricBreakdown: CaloricBreakdown,
    val calories: Double,
    val carbs: String,
    val fat: String,
    val nutrients: List<Nutrient>,
    val protein: String
)