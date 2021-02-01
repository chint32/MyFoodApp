package com.chstudios.myfoodapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chstudios.myfoodapp.data.local.model.Food
import com.chstudios.myfoodapp.data.local.model.FoodList
import com.chstudios.myfoodapp.data.local.model.FoodPersistence

@Dao
interface FoodDao {

    //Get items from Databases

    @Query("SELECT * from food_table")
    fun getAllFromDb1(): LiveData<List<Food>>

    @Query("SELECT * from food_table where owner = :belongsTo")
    fun getAllFromDb1ByListName(belongsTo: String): LiveData<List<Food>>

    @Query("SELECT * from food_persistence_table")
    fun getAllFromDb2ByInUse(): LiveData<List<FoodPersistence>>

    @Query("SELECT * from food_table where barCode = :upc and owner = :listName")
    suspend fun  getSingleFoodFromDb1(upc: String, listName: String): Food

    @Query("SELECT * from food_persistence_table where upc = :upc")
    suspend fun getSingleFoodFromDb2(upc: String): FoodPersistence


    //Check if items are actually in the databases

    @Query("SELECT EXISTS(SELECT 1 FROM food_table where barCode = :upc and owner = :listName)")
    suspend fun isCachedInDb1(upc: String, listName: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM food_persistence_table where upc = :upc)")
    suspend fun isCachedInDb2(upc: String): Boolean


    //insert items into databases

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDb1(food: Food)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDb2(foodPersistence: FoodPersistence)


    //Delete items from databases

    @Query("DELETE FROM food_table")
    suspend fun deleteAll()

    @Query("DELETE FROM food_table where barCode = :upc and owner = :listName")
    suspend fun deleteSingleFoodFromDb1(upc: String, listName: String)

    @Query("DELETE FROM food_persistence_table where upc = :upc")
    suspend fun deleteSingleFoodFromDb2(upc: String)


    //Get, insert, delete lists

    @Query("Select * from list_name")
    fun getAllLists(): LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertList(foodList: FoodList)

    @Query("Delete from list_name where listName = :listName")
    suspend fun deleteList(listName: String)
}
