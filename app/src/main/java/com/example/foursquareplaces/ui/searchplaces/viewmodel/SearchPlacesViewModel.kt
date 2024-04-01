package com.example.foursquareplaces.ui.searchplaces.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.domain.usecase.abs.SearchPlacesUseCase
import com.example.foursquareplaces.utils.MyLocationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchPlacesViewModel (
    private val useCase: SearchPlacesUseCase,
    private val locationManager: MyLocationManager
) : ViewModel() {
    private lateinit var lastKnownLocation: String
    private val _placesList = MutableStateFlow<List<PlaceUI>>(emptyList())

    val placesList = _placesList
    val error = MutableStateFlow<Boolean>(false)
    val loading = MutableStateFlow<Boolean>(false)
    fun getLastKnownLocation(): LiveData<Location?> {
        return locationManager.locationData
    }
    fun startLocationUpdates() {
        locationManager.startLocationUpdates()
    }
    fun stopLocationUpdates() {
        locationManager.stopLocationUpdates()
    }
    fun searchPlaces() {
        loading.value = true
        viewModelScope.launch {
            getLastKnownLocation().observeForever { location ->
                location?.let { currentLocation ->
                    val latitude = currentLocation.latitude
                    val longitude = currentLocation.longitude
                    viewModelScope.launch {
                        lastKnownLocation = "$latitude,$longitude"
                        val searchResponseUI = useCase.searchPlaces("$latitude,$longitude")?.places
                        stopLocationUpdates()
                        if (searchResponseUI != null) {
                            _placesList.value = searchResponseUI
                        }
                        loading.value = false
                    }
                }
            }
            startLocationUpdates()
        }
    }

    fun filterPlaces(minPrice: String?, openNow: Boolean?) {
        loading.value = true
        viewModelScope.launch {

            val searchResponseUI = minPrice?.length?.let { openNow?.let { it1 ->
                useCase.filterPlaces(it,
                    it1,
                    lastKnownLocation,
                )?.places
            } }
            if (searchResponseUI != null) {
                _placesList.value = searchResponseUI
            }
            loading.value = false
        }
    }
}
