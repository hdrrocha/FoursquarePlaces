package com.example.foursquareplaces.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
class SearchResponseTest {
    @Test
    fun testSearchResponse() {
        val place1 = Place(
            "fsq_id_1",
            100,
            Location("Address 1", "Extended Address 1", "Country 1", null, "Formatted Address 1", "Locality 1", null, "Postcode 1", "Region 1"),
            "Place 1",
            emptyList(),
            2,
            4.5f,
            true,
            emptyList(),
            Hours("Display 1", false, true),
            "123456789",
            emptyList()
        )
        val place2 = Place(
            "fsq_id_2",
            200,
            Location("Address 2", null, "Country 2", "Cross Street 2", "Formatted Address 2", "Locality 2", "Post Town 2", "Postcode 2", "Region 2"),
            "Place 2",
            emptyList(),
            3,
            4.0f,
            false,
            emptyList(),
            Hours("Display 2", true, false),
            "987654321",
            emptyList()
        )
        val places = listOf(place1, place2)
        val searchResponse = SearchResponse(places)

        assertEquals(2, searchResponse.results.size)
        assertEquals(place1, searchResponse.results[0])
        assertEquals(place2, searchResponse.results[1])
    }
}
