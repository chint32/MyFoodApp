package com.chstudios.myfoodapp.data.remote.apiresponsemodels.nutritionix


import com.google.gson.annotations.SerializedName

data class NutritionIxResponse(
    @SerializedName("allergen_contains_eggs")
    val allergenContainsEggs: Any?,
    @SerializedName("allergen_contains_fish")
    val allergenContainsFish: Any?,
    @SerializedName("allergen_contains_gluten")
    val allergenContainsGluten: Any?,
    @SerializedName("allergen_contains_milk")
    val allergenContainsMilk: Any?,
    @SerializedName("allergen_contains_peanuts")
    val allergenContainsPeanuts: Any?,
    @SerializedName("allergen_contains_shellfish")
    val allergenContainsShellfish: Any?,
    @SerializedName("allergen_contains_soybeans")
    val allergenContainsSoybeans: Any?,
    @SerializedName("allergen_contains_tree_nuts")
    val allergenContainsTreeNuts: Any?,
    @SerializedName("allergen_contains_wheat")
    val allergenContainsWheat: Any?,
    @SerializedName("brand_id")
    val brandId: String?,
    @SerializedName("brand_name")
    val brandName: String?,
    @SerializedName("item_description")
    val itemDescription: String?,
    @SerializedName("item_id")
    val itemId: String?,
    @SerializedName("item_name")
    val itemName: String?,
    @SerializedName("leg_loc_id")
    val legLocId: Int?,
    @SerializedName("nf_calcium_dv")
    val nfCalciumDv: Double?,
    @SerializedName("nf_calories")
    val nfCalories: Int?,
    @SerializedName("nf_calories_from_fat")
    val nfCaloriesFromFat: Double?,
    @SerializedName("nf_cholesterol")
    val nfCholesterol: Any?,
    @SerializedName("nf_dietary_fiber")
    val nfDietaryFiber: Any?,
    @SerializedName("nf_ingredient_statement")
    val nfIngredientStatement: String?,
    @SerializedName("nf_iron_dv")
    val nfIronDv: Double?,
    @SerializedName("nf_monounsaturated_fat")
    val nfMonounsaturatedFat: Any?,
    @SerializedName("nf_polyunsaturated_fat")
    val nfPolyunsaturatedFat: Any?,
    @SerializedName("nf_protein")
    val nfProtein: Int?,
    @SerializedName("nf_refuse_pct")
    val nfRefusePct: Any?,
    @SerializedName("nf_saturated_fat")
    val nfSaturatedFat: Any?,
    @SerializedName("nf_serving_size_qty")
    val nfServingSizeQty: Double?,
    @SerializedName("nf_serving_size_unit")
    val nfServingSizeUnit: String?,
    @SerializedName("nf_serving_weight_grams")
    val nfServingWeightGrams: Any?,
    @SerializedName("nf_servings_per_container")
    val nfServingsPerContainer: Double?,
    @SerializedName("nf_sodium")
    val nfSodium: Int?,
    @SerializedName("nf_sugars")
    val nfSugars: Int?,
    @SerializedName("nf_total_carbohydrate")
    val nfTotalCarbohydrate: Int?,
    @SerializedName("nf_total_fat")
    val nfTotalFat: Double?,
    @SerializedName("nf_trans_fatty_acid")
    val nfTransFattyAcid: Any?,
    @SerializedName("nf_vitamin_a_dv")
    val nfVitaminADv: Int?,
    @SerializedName("nf_vitamin_c_dv")
    val nfVitaminCDv: Int?,
    @SerializedName("nf_water_grams")
    val nfWaterGrams: Any?,
    @SerializedName("old_api_id")
    val oldApiId: Any?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("usda_fields")
    val usdaFields: Any?
)