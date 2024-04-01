package com.example.foursquareplaces.data.model

import org.junit.Assert.assertEquals
import org.junit.Test
class TipTest {
    @Test
    fun testTip() {
        val tip = Tip("2024-01-19", "Some text")

        assertEquals("2024-01-19", tip.created_at)
        assertEquals("Some text", tip.text)
    }
}
