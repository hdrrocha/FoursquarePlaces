package com.example.foursquareplaces.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryTest {
    @Test
    fun testCategory() {
        val icon = Icon("prefix", "suffix")

        val category = Category(1, "Category Name", "Short Name", "Plural Name", icon)

        assertEquals(1, category.id)
        assertEquals("Category Name", category.name)
        assertEquals("Short Name", category.short_name)
        assertEquals("Plural Name", category.plural_name)
        assertEquals(icon, category.icon)
    }
}
