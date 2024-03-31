package com.example.foursquareplaces.domain.usecase.abs

import com.example.foursquareplaces.domain.uimodel.PlaceUI
interface PlaceDetailsUseCase {
    suspend fun placeDetails( fsqId: String): PlaceUI?
}