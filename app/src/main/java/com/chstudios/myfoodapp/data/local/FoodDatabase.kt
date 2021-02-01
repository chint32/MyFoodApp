package com.chstudios.myfoodapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chstudios.myfoodapp.data.local.model.Food
import com.chstudios.myfoodapp.data.local.model.FoodList
import com.chstudios.myfoodapp.data.local.model.FoodPersistence

@Database(entities = [Food::class, FoodPersistence::class, FoodList::class], version = 18, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao
}