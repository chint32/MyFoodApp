package com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    val aisle: Any,
    val badges: List<String>,
    val brand: Any,
    val breadcrumbs: List<String>,
    val description: Any,
    val generatedText: String?,
    val id: Int,
    val imageType: String,
    val images: List<String>,
    val importantBadges: List<String>,
    val ingredientCount: Int,
    val ingredientList: String,
    val ingredients: List<Ingredient>,
    @SerializedName("number_of_servings")
    val numberOfServings: Double,
    val nutrition: Nutrition,
    val price: Double,
    @SerializedName("serving_size")
    val servingSize: String,
    val servings: Servings,
    val spoonacularScore: Double,
    val title: String,
    val upc: String
)