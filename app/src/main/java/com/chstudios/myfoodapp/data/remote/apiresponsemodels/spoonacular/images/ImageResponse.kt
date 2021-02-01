package com.chstudios.myfoodapp.data.remote.apiresponsemodels.spoonacular.images


data class ImageResponse(
    val expires: Long,
    val isStale: Boolean,
    val number: Int,
    val offset: Int,
    val processingTimeMs: Int,
    val products: List<Product>,
    val totalProducts: Int,
    val type: String
)