package com.example.foursquareplaces.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.foursquareplaces.R

@Composable
fun PlaceText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        maxLines = 2,
        style = MaterialTheme.typography.body1,
        color = colorResource(id = R.color.secondary_text_color_dark),
        modifier = modifier
    )
}
