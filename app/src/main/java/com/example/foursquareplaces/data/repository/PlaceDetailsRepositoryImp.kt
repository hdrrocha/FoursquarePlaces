package com.example.foursquareplaces.data.repository

import com.example.foursquareplaces.data.api.FoursquareApi
import com.example.foursquareplaces.domain.repository.PlaceDetailsRepository
class PlaceDetailsRepositoryImp(
    private val api: FoursquareApi
) : PlaceDetailsRepository {
    override suspend fun placeDetails(fsqId: String) = api.placeDetails(fsqId)
}