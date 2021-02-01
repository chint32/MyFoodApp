package com.chstudios.myfoodapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "food_table")
data class Food(
    @PrimaryKey
    val barCode: String,
    val title: String,
    val imgpath: String?,
    val owner: String,
    val cmnName: String,
    val description: String,
    val tags: String,
    val rating: Int,
    @SerializedName("num_of_servings")
    val numOfServings: Double,
    val cals: Int,
    val carbos: String,
    val fats: String,
    val calc: Double,
    val fE: Double,
    val proteen: String,
    @SerializedName("serv_size")
    val servSize: String,
    val goodTill: String
    )
