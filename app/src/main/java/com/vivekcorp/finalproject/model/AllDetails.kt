package com.vivekcorp.finalproject.model

data class AllDetails(
    val restaurantName: String,
    val dateOfOrder: String,
    val menuItem: ArrayList<Menu>
)
