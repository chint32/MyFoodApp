package com.chstudios.myfoodapp.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chstudios.myfoodapp.data.local.model.FoodList
import com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.recipes.RecipeResponse
import com.chstudios.myfoodapp.data.repository.Repository
import com.chstudios.myfoodapp.util.Event
import com.chstudios.myfoodapp.util.Resource
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(foodRepository: Repository): ViewModel() {

    private val repository = foodRepository
    val foodLists = repository.getAllLists()
    val foodInventory = repository.getAllFromDb1()

    private val _response = MutableLiveData<Event<Resource<RecipeResponse>>>()
    val response: LiveData<Event<Resource<RecipeResponse>>>
        get() = _response


    fun getRecipesFromApi(ingredients: String?) {


//        if (upc == null) {
//            _response.value = Event(Resource.error("Search word must not be null", null))
//        }
        viewModelScope.launch {
            _response.postValue(Event(Resource.loading(null)))
            val response = repository.getRecipeFromApi(ingredients, 5, 2, true)
            Log.d("Hello", response.toString())
            _response.postValue(Event(Resource.success(response!!.data)))

            Log.d("response", response.data!!.toString())

        }
    }

    fun deleteList(listName: String){
        viewModelScope.launch {
            repository.deleteList(listName)
        }
    }

    fun insertList(listName: String){
        val foodList = FoodList(listName)
        viewModelScope.launch {
            repository.insertList(foodList)
        }
    }
}