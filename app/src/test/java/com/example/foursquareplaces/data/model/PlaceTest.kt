package com.example.foursquareplaces.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
class PlaceTest {
    @Test
    fun testPlace() {
        val location = Location("Address", "Extended Address", "Country", "Cross Street", "Formatted Address", "Locality", "Post Town", "Postcode", "Region")
        val place = Place(
            "fsq_id",
            100,
            location,
            "Place",
            emptyList(),
            2,
            4.5f,
            true,
            emptyList(),
            Hours("Display", false, true),
            "123456789",
            emptyList()
        )

        assertEquals("fsq_id", place.fsq_id)
        assertEquals(100, place.distance)
        assertEquals(location, place.location)
        assertEquals("Place", place.name)
    }
}
