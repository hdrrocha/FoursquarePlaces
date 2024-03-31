package com.example.foursquareplaces.domain.mapper

import android.util.Log
import com.example.foursquareplaces.data.model.Location
import com.example.foursquareplaces.data.model.Photo
import com.example.foursquareplaces.data.model.Place
import com.example.foursquareplaces.data.model.SearchResponse
import com.example.foursquareplaces.domain.mapper.abs.SearchPlacesMapper
import com.example.foursquareplaces.domain.uimodel.LocationUI
import com.example.foursquareplaces.domain.uimodel.PhotoUI
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.domain.uimodel.SearchResponseUI
import com.example.foursquareplaces.utils.fixImageUrl
import retrofit2.Response

class SearchPlacesMapperImp: SearchPlacesMapper {
    override fun map(input: Response<SearchResponse>): SearchResponseUI? {
        val body = input.body()
        if (body != null) {
            val results = body.results?.let { mapPlacesUI(it) } ?: return null
            return SearchResponseUI(results)
        }
        return null
    }
    private fun mapPlacesUI(places: List<Place>): List<PlaceUI> {
        return places.map { place ->
            PlaceUI(
                fsq_id = place.fsq_id ?: "",
                distance = " • ${place.distance}km"  ?:"• 0km",
                location = mapLocationUI(place.location),
                name = place.name ?: "",
                photos = place.photos.map { mapPhotoUI(it) },
                price = formatPrice(place.price ?: 0),
                rating = place.rating.toString() ?: "0.0",
                open_now = place.open_now,
                categories = emptyList(),
                hours = null,
                tel = null,
                tips = emptyList(),
                profilePhotoUrl = profilePhotoUrl(place.photos)
            )
        }
    }

    private fun profilePhotoUrl(photos: List<Photo>) = photos.firstOrNull { "${it.prefix}${it.suffix}".isNotEmpty() }?.run {
        "${this.prefix}1024${this.suffix}"
    } ?: ""


    private fun mapLocationUI(location: Location?): LocationUI {
        return LocationUI(
            address = location?.address ?: "",
            addressExtended = location?.address_extended ?: "",
            formattedAddress = location?.formatted_address ?: "",
        )
    }
    private fun mapPhotoUI(photo: Photo): PhotoUI {
        return PhotoUI(
            id = photo.id ?: "",
            createdAt = photo.created_at ?: "",
            prefix = photo.prefix ?: "",
            suffix = photo.suffix ?: "",
            width = photo.width ?: 0,
            height = photo.height ?: 0
        )
    }
    private fun formatPrice(price: Int): String {
        return when (price) {
            1 -> "Cheap"
            2 -> "Moderate"
            3 -> "Expensive"
            4 -> "Very Expensive"
            else -> "Unknown"
        }
    }
}