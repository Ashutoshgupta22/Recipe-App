package com.aspark.networking

data class ObjectResponse(
    val recipes: MutableList<RecipeResponse>
)

data class RecipeResponse(
    val id: Long = 0L,
    val title: String = "",
    val image: String = "",
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val pricePerServing: Double = 0.0,
    val extendedIngredients: List<Ingredient> = emptyList(),
    val instructions: String = "",
    val equipment: List<Equipment> = emptyList(),
    val summary: String = "",
    val nutrition: Nutrition = Nutrition(),
    val badForHealthNutrition: List<NutritionItem> = emptyList(),
    val goodForHealthNutrition: List<NutritionItem> = emptyList()
)

data class Ingredient(
    val id: Long = 0L,
    val aisle: String = "",
    val image: String = "",
    val consistency: String = "",
    val name: String = "",
    val nameClean: String = "",
    val original: String = "",
    val originalName: String = "",
    val amount: Double = 0.0,
    val unit: String = "",
    val meta: List<String> = emptyList(),
    val measures: Measures = Measures()
)

data class Measures(
    val us: Measure = Measure(),
    val metric: Measure = Measure()
)

data class Measure(
    val amount: Double = 0.0,
    val unitShort: String = "",
    val unitLong: String = ""
)

data class Instruction(
    val name: String = "",
    val steps: List<Step> = emptyList()
)

data class Step(
    val number: Int = 0,
    val step: String = "",
    val ingredients: List<SimpleIngredient> = emptyList(),
    val equipment: List<SimpleEquipment> = emptyList(),
    val length: Length? = null
)

data class SimpleIngredient(
    val id: Long = 0L,
    val name: String = "",
    val localizedName: String = "",
    val image: String = ""
)

data class SimpleEquipment(
    val id: Long = 0L,
    val name: String = "",
    val localizedName: String = "",
    val image: String = ""
)

data class Length(
    val number: Int = 0,
    val unit: String = ""
)

data class Equipment(
    val id: Long = 0L,
    val name: String = "",
    val image: String = ""
)

data class Nutrition(
    val nutrients: List<NutritionItem> = emptyList(),
    val properties: List<NutritionItem> = emptyList(),
    val flavonoids: List<NutritionItem> = emptyList(),
    val ingredients: List<IngredientNutrition> = emptyList()
)

data class NutritionItem(
    val title: String = "",
    val amount: Double = 0.0,
    val unit: String = "",
    val percentOfDailyNeeds: Double = 0.0
)

data class IngredientNutrition(
    val id: Long = 0L,
    val name: String = "",
    val amount: Double = 0.0,
    val unit: String = "",
    val percentOfDailyNeeds: Double = 0.0
)


