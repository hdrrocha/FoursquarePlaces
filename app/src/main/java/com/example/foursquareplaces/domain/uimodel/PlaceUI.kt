package com.example.foursquareplaces.domain.uimodel

data class PlaceUI(
    val fsq_id: String,
    val distance: String,
    val location: LocationUI,
    val name: String,
    val photos: List<PhotoUI>,
    val price: String,
    val rating: String,
    val open_now: Boolean,
    val categories: List<CategoryUI>,
    val hours: HoursUI?,
    val tel: String,
    val tips: List<TipUI>?,
    )