package com.example.foursquareplaces.domain.mapper.abs

import com.example.foursquareplaces.data.model.SearchResponse
import com.example.foursquareplaces.domain.uimodel.SearchResponseUI
import retrofit2.Response

interface SearchPlacesMapper {
    fun map(input: Response<SearchResponse>): SearchResponseUI?
}
