package com.example.foursquareplaces.domain.usecase

import com.example.foursquareplaces.domain.mapper.abs.SearchPlacesMapper
import com.example.foursquareplaces.domain.repository.SearchPlacesRepository
import com.example.foursquareplaces.domain.usecase.abs.SearchPlacesUseCase

class SearchPlacesUseCaseImp(
    private val repository: SearchPlacesRepository,
    private val mapper: SearchPlacesMapper
): SearchPlacesUseCase {
    override suspend fun searchPlaces(latLong: String) = mapper.map(
        repository.searchPlaces(latLong)
    )
    override suspend fun filterPlaces(
        minPrice: Int,
        openNow: Boolean,
        latLong: String
    ) = mapper.map(
        repository.filterPlaces(minPrice, openNow,latLong)
    )

}