package com.vivekcorp.finalproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menuItem")
data class MenuEntities(
    @PrimaryKey val menuItem_id: Int,
    @ColumnInfo(name="menuItem_name") val menuItemName: String,
    @ColumnInfo(name= "menuItem_cost") val menuItemCost: String
)
