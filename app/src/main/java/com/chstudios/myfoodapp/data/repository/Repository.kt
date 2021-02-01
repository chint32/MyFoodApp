package com.chstudios.myfoodapp.data.repository

import androidx.lifecycle.LiveData
import com.chstudios.myfoodapp.data.local.model.Food
import com.chstudios.myfoodapp.data.local.model.FoodList
import com.chstudios.myfoodapp.data.local.model.FoodPersistence
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.nutritionix.NutritionIxResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food.ApiResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.images.ImageResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.recipes.RecipeResponse
import com.chstudios.myfoodapp.util.Resource

interface Repository {

    fun getAllLists(): LiveData<List<String>>
    suspend fun deleteList(listName: String)
    suspend fun insertList(listName: FoodList)

    fun getAllFromDb1(): LiveData<List<Food>>
    fun getAllFromDb2(): LiveData<List<FoodPersistence>>
    fun getFromDb1ByListName(belongsTo: String): LiveData<List<Food>>
    suspend fun getSingleFoodFromDb1(upc: String, listName: String): Food
    suspend fun getSingleFoodFromDb2(upc: String): FoodPersistence

    suspend fun isCachedInDb1(upc: String, listName: String): Boolean
    suspend fun isCachedInDb2(upc: String): Boolean

    suspend fun insertToDb1(food: Food)
    suspend fun insertToDb2(food: FoodPersistence)

    suspend fun deleteSingleFoodFromDb1(upc: String, listName: String)
    suspend fun deleteSingleFoodFromDb2(upc: String)
    suspend fun deleteAll()

    suspend fun getFoodFromSpoonacular(upc: String?): Resource<ApiResponse>?
    suspend fun getRecipeFromApi(
        ingredients: String?,
        number: Int,
        ranking: Int,
        ignorePantry: Boolean
    ): Resource<RecipeResponse>?
    suspend fun getImagesFromApi(
        query: String,
        number: Int
    ): Resource<ImageResponse>?

    suspend fun getFoodFromNutritionIx(upc: String): Resource<NutritionIxResponse>?

}