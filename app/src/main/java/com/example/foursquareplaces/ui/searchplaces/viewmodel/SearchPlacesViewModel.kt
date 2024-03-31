package com.example.foursquareplaces.ui.searchplaces.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.domain.usecase.abs.SearchPlacesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchPlacesViewModel (
    private val useCase: SearchPlacesUseCase

) : ViewModel() {
    private val _placesList = MutableStateFlow<List<PlaceUI>>(emptyList())
    val placesList = _placesList
    val error = MutableStateFlow<Boolean>(false)
    val loading = MutableStateFlow<Boolean>(false)
    val latitude = "-27.5928964"
    val longitude = "-48.6164225"

    fun searchPlaces() {
        loading.value = true
        viewModelScope.launch {
            val searchResponseUI = useCase.searchPlaces("$latitude,$longitude")?.places
            if (searchResponseUI != null) {
                _placesList.value = searchResponseUI
            }
            searchResponseUI?.forEach {
                if (it != null) {
                    Log.i("Helder fsq_id", it.fsq_id)
                    Log.i("Helder name", it.name)
                }
            }
            loading.value = false
        }
    }
    fun filterPlaces(minPrice: String?, openNow: Boolean?) {
        Log.i("Helder","minPrice: ${minPrice?.length}, openNow:${openNow}?" )
        loading.value = true
        viewModelScope.launch {

            val searchResponseUI = minPrice?.length?.let { openNow?.let { it1 ->
                useCase.filterPlaces(it,
                    it1,
                    "$latitude,$longitude",
                )?.places
            } }
            if (searchResponseUI != null) {
                _placesList.value = searchResponseUI
            }
            loading.value = false
        }

    }

}
