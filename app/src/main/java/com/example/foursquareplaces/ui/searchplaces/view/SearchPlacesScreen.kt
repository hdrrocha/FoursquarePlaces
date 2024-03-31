package com.example.foursquareplaces.ui.searchplaces.view


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.ui.unit.sp
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.foursquareplaces.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.ui.searchplaces.viewmodel.SearchPlacesViewModel
import com.example.foursquareplaces.utils.fixImageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchPlacesView(viewModel: SearchPlacesViewModel, navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.searchPlaces()
    }

    var placesList by remember { mutableStateOf(emptyList<PlaceUI>()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedPriceOption by remember { mutableStateOf("$$") }
    var isOpenNow by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.placesList.collect { list ->
            placesList = list ?: emptyList()
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.loading.collect { loading ->
            isLoading = loading
        }
    }

    Scaffold(
        topBar = {
            Toolbar()
        },
        backgroundColor = colorResource(id = R.color.background_color),
        content = {
            Column {
                FilterMenu(
                    selectedPriceOption = selectedPriceOption,
                    onPriceOptionSelected = { priceOption ->
                        selectedPriceOption = priceOption
                        viewModel.filterPlaces(priceOption.takeIf { it.isNotEmpty() }, isOpenNow)
                    },
                    onToggleStateChanged = { isChecked ->
                        isOpenNow = isChecked
                        viewModel.filterPlaces(selectedPriceOption.takeIf { it.isNotEmpty() }, isChecked)
                    }
                )
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                } else {
                    LazyVerticalGrid(GridCells.Fixed(2)) {
                        items(placesList) { place ->
                            ItemPlace(place = place) { itemName ->
                                // Navegar para a tela de detalhes passando o nome do item clicado
                                navController.navigate("item_detail_screen/$itemName")
                            }
                        }
                    }
                }
            }
        }
    )
}
@Composable
fun Toolbar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 22.sp,
                color = colorResource(id = R.color.nav_bar_title_color),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        backgroundColor = colorResource(id = R.color.nav_bar_color),
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
fun FilterMenu(
    selectedPriceOption: String,
    onPriceOptionSelected: (String) -> Unit,
    onToggleStateChanged: (Boolean) -> Unit,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Valor") } // Estado para controlar o texto exibido
    var isOpenNow by remember { mutableStateOf(false) } // Estado para controlar o estado do Switch

    DisposableEffect(Unit) {
        onDispose { }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .clickable { isMenuExpanded = !isMenuExpanded }
                .background(color = Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = selectedText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            modifier = Modifier.weight(1f)
        ) {
            DropdownMenuItem(onClick = {
                onPriceOptionSelected("$")
                selectedText = "Cheap"
                isMenuExpanded = false
            }) {
                Text(text = "Cheap")
            }
            DropdownMenuItem(onClick = {
                onPriceOptionSelected("$$")
                selectedText = "Moderate"
                isMenuExpanded = false
            }) {
                Text(text = "Moderate")
            }
            DropdownMenuItem(onClick = {
                onPriceOptionSelected("$$$")
                selectedText = "Expensive"
                isMenuExpanded = false
            }) {
                Text(text = "Expensive")
            }
            DropdownMenuItem(onClick = {
                onPriceOptionSelected("$$$$")
                selectedText = "Very Expensive"
                isMenuExpanded = false
            }) {
                Text(text = "Very Expensive")
            }
        }

        // Adicionando o Switch para o filtro "Abertos"
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(
                text = if (isOpenNow) "Aberto" else "Fechado",
                color = Color.Black,
                fontSize = 14.sp
            )
            Switch(
                checked = isOpenNow,
                onCheckedChange = { isChecked ->
                    isOpenNow = isChecked
                    onToggleStateChanged(isChecked)
                }
            )
        }
    }
}

@Composable
fun ItemPlace(place: PlaceUI, onItemClick: (String) -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier.size(200.dp)
        ) {
            // Exibir a primeira foto não vazia
            place.photos?.firstOrNull { "${it.prefix}${it.suffix}".isNotEmpty() }?.let { photo ->
                var photoUrl = "${photo.prefix}1024${photo.suffix}".fixImageUrl()
                GlideImage(url = photoUrl)
            }
            // Gradiente sobre a imagem
            Box(
                modifier = Modifier
                    .clickable { onItemClick(place.fsq_id) }
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xAA000000), Color(0x00000000)),
                            startY = 1f,
                            endY = 0f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = place.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    Text(
                        text = place.distance,
                        color = Color.White,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }
                Text(
                    text = place.price.toString(),
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
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
                        .placeholder(R.drawable.error_image)
                        .error(R.drawable.error_image) // Definir a imagem de erro
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
        Image(
            bitmap = img,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}