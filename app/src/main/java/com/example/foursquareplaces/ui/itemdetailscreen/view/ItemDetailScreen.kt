package com.example.foursquareplaces.ui.itemdetailscreen.view
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.runtime.remember

import android.annotation.SuppressLint
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.domain.uimodel.TipUI
import com.example.foursquareplaces.ui.itemdetailscreen.viewmodel.PlaceDetailsViewModel
import com.example.foursquareplaces.utils.fixImageUrl

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ItemDetailScreen(viewModel: PlaceDetailsViewModel, name: String, onBackPressed: () -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.searchPlaces(name)
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
                title = {
                    Text(text = "Information")
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    HeartButton()
                },
                backgroundColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = {
            if (isLoading) {
                // Mostrar o loader enquanto os dados estão sendo carregados
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else {
                // Mostrar o conteúdo da tela quando os dados estiverem disponíveis
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()), // Adiciona scroll vertical
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagem arredondada
                    place?.photos
                        ?.firstOrNull { "${it.prefix}${it.suffix}".isNotEmpty() }
                        ?.let { photo ->
                            val photoUrl = "${photo.prefix}1024${photo.suffix}".fixImageUrl()
                            GlideImage(url = photoUrl)
                        }

                    // Título do lugar
                    Text(
                        text = place?.name ?: "",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    // Label com informações das categorias
                    place?.categories?.forEach { category ->
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }


                    // Seção com informações adicionais
                    Spacer(modifier = Modifier.height(16.dp)) // Espaçamento entre as seções
                    AdditionalInfoSection(place = place)
                    Spacer(modifier = Modifier.height(16.dp))
                    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
                    LazyRow(
                        modifier = Modifier.padding(top = 16.dp).height(screenWidth).fillMaxWidth()
                    ) {
                        items(place?.photos ?: emptyList()) { photo ->
                            val photoUrl = "${photo.prefix}1024${photo.suffix}".fixImageUrl()
                            ImageGalleryItem(url = photoUrl)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenWidth)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(place?.tips ?: emptyList()) { tip ->
                            CustomerReviewItem(tip = tip)
                        }
                    }
                }
            }
        }


    )
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
private fun GlideImage(url: String) {
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
            .size(120.dp)
            .clip(shape = RoundedCornerShape(100.dp)) // Bordas arredondadas
            .background(Color.White)
            .shadow(4.dp, shape = RoundedCornerShape(5.dp)) // Sombra
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

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageHeight = screenWidth // Altura igual à largura da tela

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth() // Ocupa a largura total da tela
            .aspectRatio(1f) // Relação de aspecto 1:1 para formar um quadrado
            .clip(shape = RoundedCornerShape(10.dp)) // Bordas arredondadas
            .background(Color.White)
            .shadow(4.dp, shape = RoundedCornerShape(5.dp)) // Sombra
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
        elevation = 4.dp, // Elevação para criar a sombra
        shape = RoundedCornerShape(8.dp) // Borda arredondada do card
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


@Composable
private fun AdditionalInfoSection(place: PlaceUI?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Faixa de preço
        place?.price?.let { price ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "Faixa de Preço: ", fontWeight = FontWeight.Bold)
                // Faixa de preço
                Text(
                    text = place.price,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Classificação
        place?.rating?.let { it ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = place.rating,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Número de telefone
        place?.tel?.let { tel ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "Número de Telefone: ", fontWeight = FontWeight.Bold)
                Text(text = tel)
            }
        }

        // Endereço
        place?.location?.address?.let { address ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "Endereço: ", fontWeight = FontWeight.Bold)
                Text(text = address)
            }
        }

        // Disponibilidade
        val availabilityText = if (place?.hours?.openNow == true) "Aberto" else "Fechado"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Disponibilidade: ", fontWeight = FontWeight.Bold)
            Text(text = availabilityText)
        }
    }
}
@Composable
private fun PriceRangeText(priceRange: Int?) {
    val priceDescription = when (priceRange) {
        1 -> "Cheap"
        2 -> "Moderate"
        3 -> "Expensive"
        4 -> "Very Expensive"
        else -> "Unknown"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(text = "Faixa de Preço: ", fontWeight = FontWeight.Bold)
        Text(text = priceDescription)
    }
}
