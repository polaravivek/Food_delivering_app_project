package com.vivekcorp.finalproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Entities(
    @PrimaryKey val food_id: Int,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "food_cost") val foodCost: String,
    @ColumnInfo(name = "food_rating") val foodRating: String,
    @ColumnInfo(name = "food_image") val foodImage: String
)
