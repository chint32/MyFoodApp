package com.chstudios.myfoodapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_name")
data class FoodList (
    @PrimaryKey
    val listName: String
        )