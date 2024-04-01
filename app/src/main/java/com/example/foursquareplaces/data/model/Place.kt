package com.example.foursquareplaces.data.model

data class Place(
    val fsq_id: String,
    val distance: Int,
    val location: Location,
    val name: String,
    val photos: List<Photo>,
    val price: Int,
    val rating: Float,
    val open_now: Boolean,
    val categories: List<Category>,
    val hours: Hours,
    val tel: String?,
    val tips: List<Tip>
)