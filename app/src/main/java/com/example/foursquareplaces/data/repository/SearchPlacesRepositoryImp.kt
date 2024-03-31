package com.example.foursquareplaces.data.repository

import com.example.foursquareplaces.data.api.FoursquareApi
import com.example.foursquareplaces.domain.repository.SearchPlacesRepository

class SearchPlacesRepositoryImp(
    private val api: FoursquareApi
) : SearchPlacesRepository {
    override suspend fun searchPlaces(latLong: String)= api.searchPlaces(latLong)
    override suspend fun filterPlaces(
        minPrice: Int,
        openNow: Boolean,
        latLong: String
    )= api.filterPlaces(minPrice,openNow,latLong)
}