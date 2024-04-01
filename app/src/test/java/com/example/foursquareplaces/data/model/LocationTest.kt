package com.example.foursquareplaces.data.model
import org.junit.Assert.assertEquals
import org.junit.Test
class LocationTest {

    @Test
    fun testLocation() {
        val location = Location("Address", "Extended Address", "Country", "Cross Street", "Formatted Address", "Locality", "Post Town", "Postcode", "Region")

        assertEquals("Address", location.address)
        assertEquals("Extended Address", location.address_extended)
        assertEquals("Country", location.country)
        assertEquals("Cross Street", location.cross_street)
        assertEquals("Formatted Address", location.formatted_address)
        assertEquals("Locality", location.locality)
        assertEquals("Post Town", location.post_town)
        assertEquals("Postcode", location.postcode)
        assertEquals("Region", location.region)
    }
}
