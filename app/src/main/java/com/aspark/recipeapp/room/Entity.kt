package com.aspark.recipeapp.room

import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aspark.networking.model.Equipment
import com.aspark.networking.model.Ingredient
import com.aspark.networking.model.RecipeResponse

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val servings: Int,
    val pricePerServing: Double,
    val instructions: String,
    val summary: String,
    var isFavorite: Boolean
)

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
    )
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val ingredientId: Long = 0,
    val recipeId: Long,
    val name: String,
    val amount: Double,
    val unit: String
)

@Entity(
    tableName = "equipment",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
    )
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true) val equipmentId: Long = 0,
    val recipeId: Long,
    val name: String,
    val image: String
)

fun RecipeResponse.toEntity(): RecipeEntity {

    return RecipeEntity(
        id = this.id,
        title = this.title,
        image = this.image,
        readyInMinutes = this.readyInMinutes,
        servings = this.servings,
        pricePerServing = this.pricePerServing,
        instructions = this.instructions,
        summary = this.summary,
        isFavorite = false
    )
}


 fun RecipeEntity.toRecipeResponse(
    ingredients: List<IngredientEntity>,
    equipments: List<EquipmentEntity>
): RecipeResponse {

    val ingredientList = ingredients.map { ingredientEntity ->
        Ingredient(
            name = ingredientEntity.name,
            amount = ingredientEntity.amount,
            unit = ingredientEntity.unit
        )
    }

    val equipmentList = equipments.map { equipmentEntity ->
        Equipment(
            name = equipmentEntity.name,
            image = equipmentEntity.image
        )
    }

    return RecipeResponse(
        id,
        title,
        image,
        readyInMinutes,
        servings,
        pricePerServing,
        instructions = instructions,
        extendedIngredients = ingredientList,
        equipment = equipmentList,
        summary = summary
    )
}
