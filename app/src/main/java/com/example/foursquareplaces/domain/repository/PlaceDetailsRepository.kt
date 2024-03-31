package com.example.foursquareplaces.domain.repository

import com.example.foursquareplaces.data.model.Place
import retrofit2.Response

interface PlaceDetailsRepository {
    suspend fun placeDetails(fsqId: String): Response<Place>
}
