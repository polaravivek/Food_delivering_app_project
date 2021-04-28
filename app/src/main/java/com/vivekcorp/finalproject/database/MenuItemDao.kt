package com.vivekcorp.finalproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MenuItemDao {

    @Insert
    fun insertMenuItem(menuEntities: MenuEntities)

    @Delete
    fun deleteMenuItem(menuEntities: MenuEntities)

    @Query("SELECT * FROM menuItem")
    fun getAllMenuItems() : List<MenuEntities>

    @Query("SELECT * FROM menuItem WHERE menuItem_id= :menuItemId")
    fun getMenuItemsById(menuItemId: String): MenuEntities

    @Query("DELETE FROM menuItem")
    fun deleteTable()
}