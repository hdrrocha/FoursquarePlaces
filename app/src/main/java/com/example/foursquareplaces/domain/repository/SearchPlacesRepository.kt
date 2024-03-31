package com.example.foursquareplaces.domain.repository

import com.example.foursquareplaces.data.model.SearchResponse
import retrofit2.Response

interface SearchPlacesRepository {
    suspend fun searchPlaces(latLong: String): Response<SearchResponse>
    suspend fun filterPlaces(
        minPrice: Int,
        openNow: Boolean,
        latLong: String
    ): Response<SearchResponse>
}
