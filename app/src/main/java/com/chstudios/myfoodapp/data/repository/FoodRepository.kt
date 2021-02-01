package com.chstudios.myfoodapp.data.repository

import android.util.Log
import com.chstudios.myfoodapp.data.local.FoodDao
import com.chstudios.myfoodapp.data.local.model.Food
import com.chstudios.myfoodapp.data.local.model.FoodList
import com.chstudios.myfoodapp.data.local.model.FoodPersistence
import com.chstudios.myfoodapp.data.remote.spoonacularapi.SpoonacularApiHelper
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.nutritionix.NutritionIxResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food.ApiResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.images.ImageResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.recipes.RecipeResponse
import com.chstudios.myfoodapp.util.Resource
import javax.inject.Inject

class FoodRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val apiHelper: SpoonacularApiHelper
) : Repository {

    override fun getAllLists() = foodDao.getAllLists()
    override suspend fun deleteList(listName: String) = foodDao.deleteList(listName)
    override suspend fun insertList(listName: FoodList) = foodDao.insertList(listName)

    override fun getAllFromDb1() = foodDao.getAllFromDb1()

    override fun getFromDb1ByListName(belongsTo: String) = foodDao.getAllFromDb1ByListName(belongsTo)

    override fun getAllFromDb2() = foodDao.getAllFromDb2ByInUse()
    override suspend fun getSingleFoodFromDb1(upc: String, listName: String) = foodDao.getSingleFoodFromDb1(upc, listName)
    override suspend fun getSingleFoodFromDb2(upc: String) = foodDao.getSingleFoodFromDb2(upc)

    override suspend fun isCachedInDb1(upc: String, listName: String): Boolean = foodDao.isCachedInDb1(upc, listName)
    override suspend fun isCachedInDb2(upc: String) = foodDao.isCachedInDb2(upc)

    override suspend fun insertToDb1(food: Food) = foodDao.insertToDb1(food)
    override suspend fun insertToDb2(food: FoodPersistence) = foodDao.insertToDb2(food)

    override suspend fun deleteAll() = foodDao.deleteAll()
    override suspend fun deleteSingleFoodFromDb1(upc: String, listName: String) = foodDao.deleteSingleFoodFromDb1(upc, listName)
    override suspend fun deleteSingleFoodFromDb2(upc: String) = foodDao.deleteSingleFoodFromDb2(upc)


    override suspend fun getFoodFromSpoonacular(upc: String?): Resource<ApiResponse>? {
        return try {
            val response = apiHelper.getFoodFromSpoonacular(upc)
            if (response.isSuccessful) {
                Log.d("spoon resposne", " ${response.body()}")
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                return Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            return Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getRecipeFromApi(
        ingredients: String?,
        number: Int,
        ranking: Int,
        ignorePantry: Boolean
    ): Resource<RecipeResponse>? {
        return try {
            val response = apiHelper.getRecipeFromApi(ingredients, number, ranking, ignorePantry)
            if (response.isSuccessful) {
                Log.d("recipe resposne", " ${response.body()}")
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                return Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            return Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getImagesFromApi(query: String, number: Int): Resource<ImageResponse>? {
        return try {
            val response = apiHelper.getImagesFromAPI(query, number)
            if (response.isSuccessful) {
                Log.d("image resposne", " ${response.body()}")
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                return Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            return Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }

    override suspend fun getFoodFromNutritionIx(upc: String): Resource<NutritionIxResponse>? {

        return Resource.success(apiHelper.getFoodFromNutritionIx(upc).body())

//        return try {
//            val response = apiHelper.getFoodFromNutritionIx(upc)
//            if (response.isSuccessful) {
//                Log.d("nutrition resposne", " $response")
//                response.body()?.let {
//                    return@let Resource.success(it)
//                } ?: Resource.error("An unknown error occured", null)
//            } else {
//                return Resource.error("An unknown error occured", null)
//            }
//        } catch (e: Exception) {
//            return Resource.error("Couldn't reach the server. Check your internet connection", null)
//        }
    }
}