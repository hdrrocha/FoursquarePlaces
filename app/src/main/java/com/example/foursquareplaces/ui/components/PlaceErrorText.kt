package com.example.foursquareplaces.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import com.example.foursquareplaces.R

@Composable
fun PlateTextError(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        maxLines = 2,
        style = MaterialTheme.typography.h5,
        color = colorResource(id = R.color.primary_text_error_color),
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_error_color)),
        textAlign = TextAlign.Center,
    )
}
