package com.example.foursquareplaces.ui.itemdetailscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.domain.usecase.abs.PlaceDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
class PlaceDetailsViewModel (
    private val useCase: PlaceDetailsUseCase

) : ViewModel() {
    private val _place = MutableStateFlow<PlaceUI?>(null)
    val place = _place
    val error = MutableStateFlow<Boolean>(false)
    val loading = MutableStateFlow<Boolean>(false)
    fun searchPlaces(fsqId: String) {
        loading.value = true
        viewModelScope.launch {
            val placeDetail = useCase.placeDetails(fsqId)
            if (placeDetail != null) {
                _place.value = placeDetail
            } else {
                error.value = true
            }
            loading.value = false
        }
    }
}
