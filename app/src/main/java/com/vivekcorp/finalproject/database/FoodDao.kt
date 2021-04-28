package com.vivekcorp.finalproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {

    @Insert
    fun insertFood(foodEntity: Entities)

    @Delete
    fun deleteFood(foodEntity: Entities)

    @Query("SELECT * FROM foods")
    fun getAllFoods() : List<Entities>

    @Query("SELECT * FROM foods WHERE food_id= :foodId")
    fun getFoodById(foodId: String): Entities
}