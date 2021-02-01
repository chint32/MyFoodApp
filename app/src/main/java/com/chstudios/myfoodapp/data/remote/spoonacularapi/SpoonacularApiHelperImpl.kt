package com.chstudios.myfoodapp.data.remote.spoonacularapi

import javax.inject.Inject
import javax.inject.Named


class SpoonacularApiHelperImpl @Inject constructor(
    @Named("Spoonacular")private val api: SpoonacularAPI,
    @Named("NutritionIx")private val api2: SpoonacularAPI
) : SpoonacularApiHelper {
    override suspend fun getFoodFromSpoonacular(food: String?) = api.getFoodFromSpoonacular(food)

    override suspend fun getRecipeFromApi(
        ingredients: String?,
        number: Int,
        ranking: Int,
        ignorePantry: Boolean
    ) = api.getRecipesFromAPI(ingredients, number, ranking, ignorePantry)

    override suspend fun getImagesFromAPI(query: String, number: Int) = api.getImagesFromAPI(query, number)


    override suspend fun getFoodFromNutritionIx(upc: String) = api2.getFoodFromNutritionIx(upc)
}