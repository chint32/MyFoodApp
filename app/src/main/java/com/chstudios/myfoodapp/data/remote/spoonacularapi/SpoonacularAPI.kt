package com.chstudios.myfoodapp.data.remote.spoonacularapi
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.nutritionix.NutritionIxResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food.ApiResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.images.ImageResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.recipes.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularAPI{

    @GET("food/products/upc/{upc}")
    suspend fun getFoodFromSpoonacular(@Path("upc") upc: String?): Response<ApiResponse>

    @GET("recipes/findByIngredients")
    suspend fun getRecipesFromAPI(
        @Query("ingredients") ingredients: String?,
        @Query("number") number: Int?,
        @Query("ranking") ranking: Int?,
        @Query("ignorePantry") ignorePantry: Boolean?,
    ): Response<RecipeResponse>

    @GET("food/products/search")
    suspend fun getImagesFromAPI(
        @Query("query") query: String,
        @Query("number") number: Int
    ): Response<ImageResponse>




    @GET("item")
    suspend fun getFoodFromNutritionIx(@Query("upc") upc: String?): Response<NutritionIxResponse>
}