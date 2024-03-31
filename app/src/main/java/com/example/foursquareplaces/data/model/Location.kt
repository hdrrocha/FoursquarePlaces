package com.example.foursquareplaces.data.model

data class Location(
    val address: String?,
    val address_extended: String?,
    val country: String,
    val cross_street: String?,
    val formatted_address: String,
    val locality: String,
    val post_town: String?,
    val postcode: String,
    val region: String
)