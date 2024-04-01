package com.example.foursquareplaces.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.foursquareplaces.R
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier

@Composable
fun PlaceDivider() {
    Divider(
        color = colorResource(id = R.color.secondary_text_color_dark),
        thickness = 1.dp,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(0.8.dp)
            .fillMaxWidth()
    )
}