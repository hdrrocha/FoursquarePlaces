package com.example.foursquareplaces.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.foursquareplaces.R

@Composable
fun PlateTextTitle(text: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.h6,
            color = colorResource(id = R.color.primary_text_color_dark),
            textAlign = TextAlign.Center,
            modifier = modifier.padding(horizontal = 4.dp).align(Alignment.CenterHorizontally),
        )
    }

}
