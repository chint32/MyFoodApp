package com.chstudios.myfoodapp.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.chstudios.myfoodapp.data.local.model.Food
import com.chstudios.myfoodapp.data.local.model.FoodPersistence
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.nutritionix.NutritionIxResponse
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.food.Nutrition
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.images.ImageResponse
import com.chstudios.myfoodapp.data.repository.Repository
import com.chstudios.myfoodapp.util.Constants
import com.chstudios.myfoodapp.util.Event
import com.chstudios.myfoodapp.util.Resource
import com.chstudios.myfoodapp.util.Status
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import java.lang.Exception
import java.util.*

class InventoryViewModel @ViewModelInject constructor(
    foodRepository: Repository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val repository = foodRepository
    val foodInventory = repository.getFromDb1ByListName(savedStateHandle.get("list_name")!!)

    private val _foodResponse = MutableLiveData<Event<Resource<Food>>>()
    val foodResponse: LiveData<Event<Resource<Food>>> = _foodResponse

    private val _nutritionResponse = MutableLiveData<Event<Resource<NutritionIxResponse>>>()
    val nutritionResponse: LiveData<Event<Resource<NutritionIxResponse>>> = _nutritionResponse

    private val _imageResponse = MutableLiveData<Event<Resource<ImageResponse>>>()
    val imageResponse: LiveData<Event<Resource<ImageResponse>>> = _imageResponse

    fun addFood(upc: String, listName: String, goodTill: Date) {
        _foodResponse.value = Event(Resource.loading(null))
        viewModelScope.launch {
            if (repository.isCachedInDb1(upc, listName)) {
                val food = repository.getSingleFoodFromDb1(upc, listName)
                Log.d("viewModel", "food coming from db1: ${food.title}")
                _foodResponse.value =
                    Event(Resource.success(food))
            }

            else if(repository.isCachedInDb2(upc)) {
                val foodPers = repository.getSingleFoodFromDb2(upc)
                val food = Food(
                    foodPers.upc,
                    foodPers.foodName,
                    foodPers.imagePath,
                    listName,
                    foodPers.commonName,
                    foodPers.generatedText,
                    foodPers.importantBadges,
                    foodPers.score,
                    foodPers.numberOfServings,
                    foodPers.calories,
                    foodPers.carbs,
                    foodPers.fat,
                    foodPers.calcium,
                    foodPers.iron,
                    foodPers.protein,
                    foodPers.servingSize,
                    goodTill.toString()
                    )
                Log.d("viewModel", "food coming from db2")
                _foodResponse.value = Event(Resource.success(food))
            }

            else {
                val apiRepsonse = repository.getFoodFromSpoonacular(upc)
                try {
                    val food = Food(
                        apiRepsonse!!.data!!.upc,
                        apiRepsonse.data!!.title,
                        apiRepsonse.data.images[0],
                        listName,
                        apiRepsonse.data.breadcrumbs[0],
                        apiRepsonse.data.generatedText ?: apiRepsonse.data.description.toString(),
                        apiRepsonse.data.importantBadges.toString(),
                        apiRepsonse.data.spoonacularScore.toInt(),
                        apiRepsonse.data.numberOfServings,
                        apiRepsonse.data.nutrition.calories.toInt(),
                        apiRepsonse.data.nutrition.carbs,
                        apiRepsonse.data.nutrition.fat,
                        apiRepsonse.data.nutrition.nutrients[0].amount,
                        apiRepsonse.data.nutrition.nutrients[4].amount,
                        apiRepsonse.data.nutrition.protein,
                        apiRepsonse.data.servingSize,
                        goodTill.toString()
                    )
                    _foodResponse.value = Event(Resource.success(food))
                } catch (e: Exception){
                    Log.d("exception", e.message)
                    _foodResponse.value = Event(Resource.error("unknown error", null))
                }
            }
        }
    }

    fun getFoodFromNutritionIx(query: String) {
        _nutritionResponse.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val nutritionIxResponse = repository.getFoodFromNutritionIx(query)!!
            _nutritionResponse.value = Event(nutritionIxResponse)
        }
    }

    fun getImagesFromApi(query: String, numberOfItems: Int) {
        _imageResponse.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val imgResponse = repository.getImagesFromApi(query, numberOfItems)!!
            _imageResponse.value = Event(imgResponse)
        }
    }

    fun deleteFood(food: Food, listName: String) {
        viewModelScope.launch {
            if(repository.isCachedInDb1(food.barCode, listName))
                repository.deleteSingleFoodFromDb1(food.barCode, listName)
        }
    }

    fun insertToDb1(food: Food) {
        viewModelScope.launch {
            repository.insertToDb1(food)
        }
    }

    fun insertToDb2(food: FoodPersistence) {
        viewModelScope.launch {
            repository.insertToDb2(food)
        }
    }
}