package com.example.foursquareplaces.domain.usecase

import com.example.foursquareplaces.domain.mapper.abs.PlaceDetailsMapper
import com.example.foursquareplaces.domain.repository.PlaceDetailsRepository
import com.example.foursquareplaces.domain.usecase.abs.PlaceDetailsUseCase

class PlaceDetailsUseCaseImp(
    private val repository: PlaceDetailsRepository,
    private val mapper: PlaceDetailsMapper
): PlaceDetailsUseCase {
    override suspend fun placeDetails(fsqId: String) = mapper.map(
        repository.placeDetails(fsqId).body()
    )
}