package com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food


data class Nutrient(
    val amount: Double,
    val percentOfDailyNeeds: Double,
    val title: String,
    val unit: String
)