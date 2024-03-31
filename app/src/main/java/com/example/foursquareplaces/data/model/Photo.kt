package com.example.foursquareplaces.data.model

data class Photo(
    val id: String,
    val created_at: String,
    val prefix: String,
    val suffix: String,
    val width: Int,
    val height: Int,
    val classifications: List<String>? = null
)