package com.example.foursquareplaces.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
class IconTest {
    @Test
    fun testIcon() {
        val icon = Icon("prefix", "suffix")

        assertEquals("prefix", icon.prefix)
        assertEquals("suffix", icon.suffix)
    }
}
