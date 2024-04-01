package com.example.foursquareplaces.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
class PhotoTest {
    @Test
    fun testPhoto() {
        val photo = Photo("123", "2024-09-19", "prefix", "suffix", 100, 200, null)

        assertEquals("123", photo.id)
        assertEquals("2024-09-19", photo.created_at)
        assertEquals("prefix", photo.prefix)
        assertEquals("suffix", photo.suffix)
        assertEquals(100, photo.width)
        assertEquals(200, photo.height)
        assertEquals(null, photo.classifications)
    }
}
