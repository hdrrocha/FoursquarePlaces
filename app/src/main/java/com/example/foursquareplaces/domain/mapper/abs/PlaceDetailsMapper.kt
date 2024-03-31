package com.example.foursquareplaces.domain.mapper.abs

import com.example.foursquareplaces.data.model.Place
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import retrofit2.Response
interface PlaceDetailsMapper {
    fun map(input: Place?): PlaceUI?
}
