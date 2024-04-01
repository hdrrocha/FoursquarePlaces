package com.example.foursquareplaces.domain.usecase.abs

import com.example.foursquareplaces.domain.uimodel.SearchResponseUI

interface SearchPlacesUseCase {
    suspend fun searchPlaces(latLong: String): SearchResponseUI?
    suspend fun filterPlaces(
        minPrice: Int,
        openNow: Boolean,
        latLong: String
    ): SearchResponseUI?
}