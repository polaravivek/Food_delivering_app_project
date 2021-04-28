package com.vivekcorp.finalproject.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entities::class], version = 1)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao
}