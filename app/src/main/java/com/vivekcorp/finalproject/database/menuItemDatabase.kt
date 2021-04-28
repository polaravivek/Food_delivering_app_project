package com.vivekcorp.finalproject.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MenuEntities::class],version = 1)
abstract class menuItemDatabase : RoomDatabase() {

    abstract fun menuItemDao(): MenuItemDao
}