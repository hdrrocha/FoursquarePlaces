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
import androidx.compose.foundation.clickable
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
import com.example.foursquareplaces.ui.components.PlaceDivider
import com.example.foursquareplaces.ui.components.PlaceRoundImage
import com.example.foursquareplaces.ui.components.PlaceText
import com.example.foursquareplaces.ui.components.PlaceTextSubTitle
import com.example.foursquareplaces.ui.components.PlateTextError
import com.example.foursquareplaces.ui.components.PlateTextTitle
import com.example.foursquareplaces.ui.itemdetailscreen.viewmodel.PlaceDetailsViewModel
import com.example.foursquareplaces.utils.fixImageUrl
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ItemDetailScreen(viewModel: PlaceDetailsViewModel, fsqId: String, onBackPressed: () -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.searchPlaces(fsqId)
    }
    val place by viewModel.place.collectAsState(null)
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.loading.collect { loading ->
            isLoading = loading
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.error.collect { error ->
            isError = error
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
                if (!isError)
                    place?.let { it1 -> DetailContent(placeInfo = it1) }
                else
                    PlateTextError(text = stringResource(id = R.string.error_default),
                        modifier = Modifier
                            .padding(horizontal = 1.dp))
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
                .background(colorResource(id = R.color.background_color))
                .align(Alignment.BottomStart)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                placeInfo.photos.firstOrNull()?.let { "${it.prefix}1024${it.suffix}".fixImageUrl() ?: "" }
                    ?.let {
                        PlaceRoundImage(url = it, Modifier.size(200.dp))
                    }
            }
            PlateTextTitle(text = placeInfo.name,modifier = Modifier.fillMaxWidth())
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    placeInfo?.categories?.forEach { category ->
                        PlaceText(  text = category.name,modifier = Modifier
                            .padding(horizontal = 4.dp) )
                    }
                }
            }



            PlaceText(  text =placeInfo.location.address,modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.CenterHorizontally) )

            PlaceText(  text =placeInfo.tel,modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.CenterHorizontally) )
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    PlaceText(  text =placeInfo.price,modifier = Modifier
                        .padding(horizontal = 4.dp))

                    PlaceText(  text = if (placeInfo.hours?.openNow == true)stringResource(id = R.string.open) else stringResource(id = R.string.closed),modifier = Modifier
                        .padding(horizontal = 4.dp))

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = colorResource(id = R.color.secondary_text_color_dark),
                        modifier = Modifier.size(16.dp)
                    )
                    PlaceText(  text =placeInfo.rating,modifier = Modifier)
                }
            }
            if(placeInfo.photos?.isNotEmpty() == true)
                PlaceTextSubTitle(text = stringResource(id = R.string.image_gallery),modifier = Modifier
                    .padding(horizontal = 4.dp))
                PlaceDivider()
                GalleryCarousel(images = placeInfo.photos.map { "${it.prefix}1024${it.suffix}".fixImageUrl() })

            if(placeInfo.tips?.isNotEmpty() == true)
                PlaceTextSubTitle(text = stringResource(id = R.string.customer_reviews),modifier = Modifier
                    .padding(horizontal = 8.dp))
                PlaceDivider()
                placeInfo.tips?.let { CustomerReviewCarousel(it) }
        }
    }
}

@Composable
private fun GalleryCarousel(images: List<String>) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LazyRow(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
    ) {
        items(images ?: emptyList()) { photo ->
            val photoUrl = photo
            ImageGalleryItem(url = photoUrl)
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
fun ImageCarousel(images: List<String>) {
    val selectedImageIndex = remember { mutableStateOf(0) }

    LazyRow(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        items(images.size) { index ->
            val isSelected = index == selectedImageIndex.value
            ImageGalleryItem(
                imageUrl = images[index],
                isSelected = isSelected,
                onImageClicked = { selectedImageIndex.value = index }
            )
        }
    }
}

@Composable
private fun ImageGalleryItem(
    imageUrl: String,
    isSelected: Boolean,
    onImageClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .height(140.dp)
            .width(240.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.background_color))
            .clickable { onImageClicked() }
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(color = Color.Blue)
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
            .height(220.dp)
            .aspectRatio(1f)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.background_color))
            .shadow(4.dp, shape = RoundedCornerShape(5.dp))
    ) {
        imageBitmap.value?.let { img ->
            Image(
                bitmap = img,
                contentDescription = "Category Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}
@Composable
private fun CustomerReviewItem(tip: TipUI) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(colorResource(id = R.color.background_card_color)),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.background_card_color))
        ) {
            Row ( modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
                .align(Alignment.CenterHorizontally)) {
                PlaceText(  text =tip.text,modifier = Modifier)
            }
            Row ( modifier = Modifier
                .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp)

                .align(Alignment.CenterHorizontally)) {
                PlaceText( text =tip.createdAt,modifier = Modifier)
            }


        }
    }
}