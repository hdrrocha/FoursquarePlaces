package com.example.foursquareplaces.ui.itemdetailscreen.view

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.runtime.remember
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.foursquareplaces.R
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.domain.uimodel.TipUI
import com.example.foursquareplaces.ui.components.PlaceRoundImage
import com.example.foursquareplaces.ui.itemdetailscreen.viewmodel.PlaceDetailsViewModel
import com.example.foursquareplaces.utils.fixImageUrl

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ItemDetailScreen(viewModel: PlaceDetailsViewModel, fsqId: String, onBackPressed: () -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.searchPlaces(fsqId)
    }
    val place by viewModel.place.collectAsState(null)
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel) {
        viewModel.loading.collect { loading ->
            isLoading = loading
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.information_app)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = { HeartButton() },
                backgroundColor = colorResource(id = R.color.nav_bar_color),
                contentColor = colorResource(id = R.color.nav_bar_title_color),
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else {
                place?.let { it1 -> DetailContent(placeInfo = it1) }
            }
        }
    )
}
@Composable
private fun DetailContent(placeInfo: PlaceUI) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(16.dp)
                .background(colorResource(id = R.color.background_color))
                .align(Alignment.BottomStart)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                placeInfo.photos.firstOrNull()?.let { "${it.prefix}1024${it.suffix}".fixImageUrl() ?: "" }
                    ?.let {
                        PlaceRoundImage(url = it, Modifier.size(200.dp))
                    }
            }

            Text(
                text = placeInfo.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h4,
                color = colorResource(id = R.color.primary_text_color_dark),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )
            placeInfo?.categories?.forEach { category ->
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.h5,
                    color = colorResource(id = R.color.secondary_text_color_dark),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Text(
                text = placeInfo.location.address,
                style = MaterialTheme.typography.h6,
                color = colorResource(id = R.color.secondary_text_color_dark),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = placeInfo.tel,
                style = MaterialTheme.typography.h6,
                color = colorResource(id = R.color.secondary_text_color_dark),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = placeInfo.price,
                    style = MaterialTheme.typography.h6,
                    color = colorResource(id = R.color.secondary_text_color_dark),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )

                Text(
                    text = if (placeInfo.hours?.openNow == true)stringResource(id = R.string.open) else stringResource(id = R.string.closed),
                    style = MaterialTheme.typography.h6,
                    color = colorResource(id = R.color.secondary_text_color_dark),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = colorResource(id = R.color.secondary_text_color_dark),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = placeInfo.rating,
                    style = MaterialTheme.typography.h6,
                    color = colorResource(id = R.color.secondary_text_color_dark),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
            }

            Text(
                text = stringResource(id = R.string.image_gallery) ,
                style = MaterialTheme.typography.h5,
                color = colorResource(id = R.color.primary_text_color_dark),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            )
            GalleryCarousel(images = placeInfo.photos.map { "${it.prefix}1024${it.suffix}".fixImageUrl() })
            Text(
                text = stringResource(id = R.string.customer_reviews),
                style = MaterialTheme.typography.h5,
                color = colorResource(id = R.color.primary_text_color_dark),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            )
            placeInfo.tips?.let { CustomerReviewCarousel(it) }
        }
    }
}

@Composable
private fun GalleryCarousel(images: List<String>) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LazyRow(
        modifier = Modifier
            .padding(top = 16.dp)
            .height(screenWidth)
            .fillMaxWidth()
    ) {
        items(images ?: emptyList()) { photo ->
            val photoUrl = photo
            ImageGalleryItem(url = photoUrl)
        }
    }
}

@Composable
private fun CustomerReviewCarousel(tips: List<TipUI>) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LazyColumn(
        modifier = Modifier
            .height(screenWidth)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(tips) { tip ->
            CustomerReviewItem(tip = tip)
        }
    }
}
@Composable
fun HeartButton() {
    var isSelected by remember { mutableStateOf(false) }

    IconButton(
        onClick = { isSelected = !isSelected },
        modifier = Modifier.padding(end = 16.dp)
    ) {
        Icon(
            imageVector = if (isSelected) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isSelected) Color.Red else Color.Gray
        )
    }
}
@Composable
private fun GlideImage(url: String, modifier: Modifier) {
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

    Box(
        modifier = modifier
    ) {
        imageBitmap.value?.let { img ->
            Image(
                bitmap = img,
                contentDescription = "Category Image",
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentScale = ContentScale.Crop
            )
        }
    }
}
@Composable
private fun ImageGalleryItem(url: String) {
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

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.background_color))
            .shadow(4.dp, shape = RoundedCornerShape(5.dp))
    ) {
        imageBitmap.value?.let { img ->
            Image(
                bitmap = img,
                contentDescription = "Category Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
@Composable
private fun CustomerReviewItem(tip: TipUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = tip.text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Date: ${tip.createdAt}",
                style = MaterialTheme.typography.caption,
                color = Color.Gray
            )
        }
    }
}