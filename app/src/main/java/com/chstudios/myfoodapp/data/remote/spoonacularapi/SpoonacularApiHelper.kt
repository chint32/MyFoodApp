package com.chstudios.myfoodapp.data.remote.spoonacularapi
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.nutritionix.NutritionIxResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food.ApiResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.images.ImageResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.recipes.RecipeResponse
import retrofit2.Response

interface SpoonacularApiHelper {
    suspend fun getFoodFromSpoonacular(word: String?): Response<ApiResponse>
    suspend fun getRecipeFromApi(
        ingredients: String?,
        number: Int,
        ranking: Int,
        ignorePantry: Boolean
    ): Response<RecipeResponse>
    suspend fun getImagesFromAPI(
        query: String,
        number: Int
    ): Response<ImageResponse>

    suspend fun getFoodFromNutritionIx(upc: String): Response<NutritionIxResponse>

}