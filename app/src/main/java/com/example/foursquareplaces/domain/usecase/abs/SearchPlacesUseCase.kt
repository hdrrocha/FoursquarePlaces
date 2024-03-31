package com.example.foursquareplaces.domain.usecase.abs

import com.example.foursquareplaces.data.model.SearchResponse
import com.example.foursquareplaces.domain.mapper.abs.SearchPlacesMapper
import com.example.foursquareplaces.domain.repository.SearchPlacesRepository
import com.example.foursquareplaces.domain.uimodel.SearchResponseUI
import retrofit2.Response

interface SearchPlacesUseCase {
    suspend fun searchPlaces(latLong: String): SearchResponseUI?
    suspend fun filterPlaces(
        minPrice: Int,
        openNow: Boolean,
        latLong: String
    ): SearchResponseUI?
}