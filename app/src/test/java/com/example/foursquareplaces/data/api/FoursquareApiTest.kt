package com.example.foursquareplaces.data.api
import com.example.foursquareplaces.BuildConfig
import com.example.foursquareplaces.data.model.Category
import com.example.foursquareplaces.data.model.Hours
import com.example.foursquareplaces.data.model.Icon
import com.example.foursquareplaces.data.model.Location
import com.example.foursquareplaces.data.model.Photo
import com.example.foursquareplaces.data.model.Place
import com.example.foursquareplaces.data.model.SearchResponse
import com.example.foursquareplaces.data.model.Tip
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Mockito.`when`
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class FoursquareApiTest {
        val place1 = Place(
        fsq_id = "1",
        distance = 1000,
        location = Location(
            address = "123 Main St",
            address_extended = null,
            country = "USA",
            cross_street = null,
            formatted_address = "123 Main St, New York, USA",
            locality = "New York",
            post_town = null,
            postcode = "10001",
            region = "NY"
        ),
        name = "Example Place 1",
        photos = listOf(
            Photo(
                id = "1",
                created_at = "2024-01-20",
                prefix = "https://example.com/photo1",
                suffix = ".jpg",
                width = 800,
                height = 600
            )
        ),
        price = 2,
        rating = 4.5f,
        open_now = true,
        categories = listOf(
            Category(
                id = 1,
                name = "Restaurant",
                short_name = "Restaurant",
                plural_name = "Restaurants",
                icon = Icon("1", "https://example.com/icon1")
            )
        ),
        hours = Hours(
            display = "Mon-Fri: 9am-5pm",
            is_local_holiday = false,
            open_now = true
        ),
        tel = "123-456-7890",
        tips = listOf(
            Tip(
                created_at = "2024-01-20",
                text = "Great place to eat!"
            )
        )
    )

    val place2 = Place(
        fsq_id = "2",
        distance = 2000,
        location = Location(
            address = "456 Elm St",
            address_extended = null,
            country = "USA",
            cross_street = null,
            formatted_address = "456 Elm St, New York, USA",
            locality = "New York",
            post_town = null,
            postcode = "10002",
            region = "NY"
        ),
        name = "Example Place 2",
        photos = listOf(
            Photo(
                id = "2",
                created_at = "2024-01-21",
                prefix = "https://example.com/photo2",
                suffix = ".jpg",
                width = 800,
                height = 600
            )
        ),
        price = 3,
        rating = 4.0f,
        open_now = false,
        categories = listOf(
            Category(
                id = 2,
                name = "Coffee Shop",
                short_name = "Coffee Shop",
                plural_name = "Coffee Shops",
                icon = Icon("2", "https://example.com/icon2")
            )
        ),
        hours = Hours(
            display = "Mon-Sun: 8am-8pm",
            is_local_holiday = false,
            open_now = false
        ),
        tel = "987-654-3210",
        tips = listOf(
            Tip(
                created_at = "2024-01-21",
                text = "Good coffee!"
            )
        )
    )

    @Mock
    private lateinit var mockFoursquareApi: FoursquareApi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testSearchPlaces() {
        runBlocking {
            val mockResponse = SearchResponse(listOf(place1, place2))

            `when`(mockFoursquareApi.searchPlaces(
                latLong = "lat,long",
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                version = BuildConfig.VERSION_API,
                query = BuildConfig.FLAVOR_SELECTED,
                radius = BuildConfig.RADIUS,
                fields = BuildConfig.SEARCH_FIELDS
            )).thenReturn(Response.success(mockResponse))

            val response = mockFoursquareApi.searchPlaces("lat,long")

            assertEquals(mockResponse, response.body())
        }
    }

    @Test
    fun testFilterPlaces() {
        runBlocking {
            val mockResponse = SearchResponse(listOf(place1, place2))

            `when`(mockFoursquareApi.filterPlaces(
                minPrice = 1,
                openNow = true,
                latLong = "lat,long",
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                version = BuildConfig.VERSION_API,
                query = BuildConfig.FLAVOR_SELECTED,
                radius = BuildConfig.RADIUS,
                fields = BuildConfig.SEARCH_FIELDS
            )).thenReturn(Response.success(mockResponse))

            val response = mockFoursquareApi.filterPlaces(1, true, "lat,long")

            assertEquals(mockResponse, response.body())
        }
    }

    @Test
    fun testPlaceDetails() {
        runBlocking {
            val mockResponse = place1

            `when`(mockFoursquareApi.placeDetails(
                fsq_id = "123",
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                version = BuildConfig.VERSION_API,
                query = BuildConfig.FLAVOR_SELECTED,
                radius = BuildConfig.RADIUS,
                fields = BuildConfig.DETAILS_FIELDS
            )).thenReturn(Response.success(mockResponse))

            val response = mockFoursquareApi.placeDetails("123")


            assertEquals(mockResponse, response.body())
        }
    }
    @Test
    fun testPlaceDetailsWithError400() {
        runBlocking {
            val errorResponse = Response.error<Place>(400, ResponseBody.create("application/json".toMediaTypeOrNull(), "Error body"))

            `when`(mockFoursquareApi.placeDetails(
                fsq_id = "123",
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                version = BuildConfig.VERSION_API,
                query = BuildConfig.FLAVOR_SELECTED,
                radius = BuildConfig.RADIUS,
                fields = BuildConfig.DETAILS_FIELDS
            )).thenReturn(errorResponse)

            val response = mockFoursquareApi.placeDetails("123")

            assertNull(response.body())
            assertEquals(400, response.code())
        }
    }

    @Test
    fun testPlaceDetailsWithError404() {
        runBlocking {
            val errorResponse = Response.error<Place>(404, ResponseBody.create("application/json".toMediaTypeOrNull(), "Error body"))

            `when`(mockFoursquareApi.placeDetails(
                fsq_id = "123",
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                version = BuildConfig.VERSION_API,
                query = BuildConfig.FLAVOR_SELECTED,
                radius = BuildConfig.RADIUS,
                fields = BuildConfig.DETAILS_FIELDS
            )).thenReturn(errorResponse)

            val response = mockFoursquareApi.placeDetails("123")

            assertNull(response.body())
            assertEquals(404, response.code())
        }
    }
}
