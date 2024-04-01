package com.example.foursquareplaces.domain.usecase
import com.example.foursquareplaces.data.model.Category
import com.example.foursquareplaces.data.model.Hours
import com.example.foursquareplaces.data.model.Icon
import com.example.foursquareplaces.data.model.Location
import com.example.foursquareplaces.data.model.Photo
import com.example.foursquareplaces.data.model.Place
import com.example.foursquareplaces.data.model.Tip
import com.example.foursquareplaces.domain.mapper.abs.PlaceDetailsMapper
import com.example.foursquareplaces.domain.repository.PlaceDetailsRepository
import com.example.foursquareplaces.domain.uimodel.CategoryUI
import com.example.foursquareplaces.domain.uimodel.HoursUI
import com.example.foursquareplaces.domain.uimodel.IconUI
import com.example.foursquareplaces.domain.uimodel.LocationUI
import com.example.foursquareplaces.domain.uimodel.PhotoUI
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.domain.uimodel.TipUI
import com.example.foursquareplaces.domain.usecase.abs.PlaceDetailsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

import retrofit2.Response

class PlaceDetailsUseCaseTest {

    // Mocks
    @Mock
    private lateinit var repository: PlaceDetailsRepository

    @Mock
    private lateinit var mapper: PlaceDetailsMapper

    // Class under test
    private lateinit var useCase: PlaceDetailsUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCase = PlaceDetailsUseCaseImp(repository, mapper)
    }

    @Test
    fun `placeDetails() should return mapped PlaceUI`() = runBlocking {
        // Mocked data
        val fsqId = "some_id"
// Mocked Location object
        val mockedLocation = Location(
            address = "Mocked Address",
            address_extended = "Mocked Extended Address",
            country = "Mocked Country",
            cross_street = "Mocked Cross Street",
            formatted_address = "Mocked Formatted Address",
            locality = "Mocked Locality",
            post_town = "Mocked Post Town",
            postcode = "Mocked Postcode",
            region = "Mocked Region"
        )

// Mocked Photo object
        val mockedPhoto = Photo(
            id = "mocked_photo_id",
            created_at = "2022-01-01T00:00:00",
            prefix = "https://mocked_prefix.com/",
            suffix = ".jpg",
            width = 100,
            height = 100,
            classifications = listOf("classification1", "classification2")
        )

// Mocked Icon object
        val mockedIcon = Icon("https://mocked_icon_prefix.com/", ".png")

// Mocked Category object
        val mockedCategory = Category(
            id = 1,
            name = "Mocked Category",
            short_name = "Mocked Short Name",
            plural_name = "Mocked Plural Name",
            icon = mockedIcon
        )

// Mocked Tip object
        val mockedTip = Tip(
            created_at = "2022-01-01T00:00:00",
            text = "Mocked Tip Text"
        )

// Mocked IconUI object
        val mockedIconUI = IconUI("https://mocked_icon_prefix.com/", ".png")

// Mocked LocationUI object
        val mockedLocationUI = LocationUI(
            address = "Mocked Address",
            addressExtended = "Mocked Extended Address",
            formattedAddress = "Mocked Formatted Address"
        )

// Mocked PhotoUI object
        val mockedPhotoUI = PhotoUI(
            id = "mocked_photo_id",
            createdAt = "2022-01-01T00:00:00",
            prefix = "https://mocked_prefix.com/",
            suffix = ".jpg",
            width = 100,
            height = 100
        )

// Mocked CategoryUI object
        val mockedCategoryUI = CategoryUI(
            id = 1,
            name = "Mocked Category",
            shortName = "Mocked Short Name",
            pluralName = "Mocked Plural Name",
            icon = mockedIconUI
        )

// Mocked TipUI object
        val mockedTipUI = TipUI(
            createdAt = "2022-01-01T00:00:00",
            text = "Mocked Tip Text"
        )
// Mocked Place object
        val mockedPlace = Place(
            fsq_id = "mocked_fsq_id",
            distance = 100, // Example distance value
            location = mockedLocation,
            name = "Mocked Place",
            photos = listOf(mockedPhoto),
            price = 2, // Example price value
            rating = 4.5f, // Example rating value
            open_now = true,
            categories = listOf(mockedCategory), // Example category
            hours = Hours("", true, false), // Example hours
            tel = "123456789", // Example telephone number
            tips = listOf(mockedTip) // Example tip
        )

// Mocked PlaceUI object
        val mockedPlaceUI = PlaceUI(
            fsq_id = "mocked_fsq_id",
            distance = "100 meters", // Example distance value
            location = mockedLocationUI, // Example LocationUI object
            name = "Mocked Place",
            photos = listOf(mockedPhotoUI),
            price = "$$", // Example price value
            rating = "4.5", // Example rating value
            open_now = true,
            categories = listOf(mockedCategoryUI), // Example category
            hours = HoursUI("", true, false), // Example hours
            tel = "123456789", // Example telephone number
            tips = listOf(mockedTipUI) // Example tip
        )


        // Stub repository method to return response
        val mockedResponse = Response.success(mockedPlace)
        `when`(repository.placeDetails(fsqId)).thenReturn(mockedResponse)

        // Stub mapper to return mapped PlaceUI
        `when`(mapper.map(mockedResponse.body())).thenReturn(mockedPlaceUI)

        // Call use case
        val result = useCase.placeDetails(fsqId)

        // Verify if mapper was called with correct arguments
        verify(mapper).map(mockedResponse.body())

        // Verify if repository method was called with correct arguments
        verify(repository).placeDetails(fsqId)

        // Assert result
        assertEquals(mockedPlaceUI, result)
    }

    @Test
    fun `placeDetails() should return null if repository response is null`() = runBlocking {
        // Mocked data
        val fsqId = "some_id"
        val mockedResponse: Response<Place>? = null

        // Stub repository method to return null response
        `when`(repository.placeDetails(fsqId)).thenReturn(mockedResponse)

        // Call use case
        val result = useCase.placeDetails(fsqId)

        // Verify if mapper was not called
        verifyZeroInteractions(mapper)

        // Verify if repository method was called with correct arguments
        verify(repository).placeDetails(fsqId)

        // Assert result is null
        assertEquals(null, result)
    }
}
