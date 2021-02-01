package com.chstudios.myfoodapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "food_persistence_table")
data class FoodPersistence(
    @PrimaryKey
    val upc: String,
    val foodName: String,
    val imagePath: String?,
    var belongsTo: String,
    val commonName: String,
    val generatedText: String,
    val importantBadges: String,
    val score: Int,
    @SerializedName("number_of_servings")
    val numberOfServings: Double,
    val calories: Int,
    val carbs: String,
    val fat: String,
    val calcium: Double,
    val iron: Double,
    val protein: String,
    @SerializedName("serving_size")
    val servingSize: String,
    val expirationDate: String
)