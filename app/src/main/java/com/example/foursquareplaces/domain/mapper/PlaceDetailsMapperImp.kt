package com.example.foursquareplaces.domain.mapper

import com.example.foursquareplaces.data.model.Category
import com.example.foursquareplaces.data.model.Hours
import com.example.foursquareplaces.data.model.Icon
import com.example.foursquareplaces.data.model.Location
import com.example.foursquareplaces.data.model.Photo
import com.example.foursquareplaces.data.model.Place
import com.example.foursquareplaces.data.model.Tip
import com.example.foursquareplaces.domain.mapper.abs.PlaceDetailsMapper
import com.example.foursquareplaces.domain.uimodel.CategoryUI
import com.example.foursquareplaces.domain.uimodel.HoursUI
import com.example.foursquareplaces.domain.uimodel.IconUI
import com.example.foursquareplaces.domain.uimodel.LocationUI
import com.example.foursquareplaces.domain.uimodel.PhotoUI
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.domain.uimodel.TipUI


class PlaceDetailsMapperImp: PlaceDetailsMapper {
    private fun formatPrice(price: Int): String {
        return when (price) {
            1 -> "Cheap"
            2 -> "Moderate"
            3 -> "Expensive"
            4 -> "Very Expensive"
            else -> "Price Range"
        }
    }
    private fun mapCategory(category: Category): CategoryUI {
        return CategoryUI(
            id = category.id,
            name = category.name,
            shortName = category.short_name,
            pluralName = category.plural_name,
            icon = mapIcon(category.icon)
        )
    }
    private fun mapIcon(icon: Icon): IconUI {
        return IconUI(
            prefix = icon.prefix,
            suffix = icon.suffix
        )
    }
    private fun mapHours(hours: Hours?): HoursUI? {
        return hours?.let {
            HoursUI(
                display = it.display ?: "" ,
                isLocalHoliday = hours.is_local_holiday,
                openNow = hours.open_now
            )
        }
    }
    private fun mapTip(tip: Tip): TipUI {
        return TipUI(
            createdAt = tip?.created_at ?: "",
            text = tip?.text?: ""
        )
    }
    private fun mapLocationUI(location: Location?): LocationUI {
        return LocationUI(
            address = location?.address ?: "",
            addressExtended = location?.address_extended ?: "",
            formattedAddress = location?.formatted_address ?: ""
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
    override fun map(input: Place?): PlaceUI? {
        return input.let { place ->
            PlaceUI(
                fsq_id = place?.fsq_id ?: "",
                distance = " • ${place?.distance}km"  ?:"• 0km",
                location = mapLocationUI(place?.location),
                name = place?.name ?: "",
                photos = place?.photos?.map { mapPhotoUI(it) } ?: emptyList(),
                price = formatPrice(place?.price ?: 0),
                rating = place?.rating.toString() ?: "0.0",
                open_now = place?.open_now ?: false,
                categories = place?.categories?.map { mapCategory(it) } ?: emptyList(),
                hours = mapHours(place?.hours),
                tel = place?.tel ?: ""  ,
                tips = place?.tips?.map { mapTip(it) }
            )
        }
    }


}