package com.example.foursquareplaces.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.foursquareplaces.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun PlaceRoundImage(url: String, modifier: Modifier = Modifier,) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(url) {
        coroutineScope.launch {
            try {
                val bitmap = withContext(Dispatchers.IO) {
                    Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .placeholder(R.drawable.error_image)
                        .error(R.drawable.error_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .submit()
                        .get()
                }
                imageBitmap.value = bitmap.asImageBitmap()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    imageBitmap.value?.let { img ->
        Surface(
            shape = RoundedCornerShape(110.dp),
            elevation = 4.dp,
            border = null,
            modifier = modifier
        ){
            Image(
                bitmap = img,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}