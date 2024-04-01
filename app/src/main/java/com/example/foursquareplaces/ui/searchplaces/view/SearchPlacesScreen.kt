package com.example.foursquareplaces.ui.searchplaces.view

import android.annotation.SuppressLint
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
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.foursquareplaces.domain.uimodel.PlaceUI
import com.example.foursquareplaces.ui.components.PlaceRoundImage
import com.example.foursquareplaces.ui.searchplaces.viewmodel.SearchPlacesViewModel
import com.example.foursquareplaces.utils.fixImageUrl

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
                    CircularProgressIndicator(modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally))
                } else {
                    LazyVerticalGrid(GridCells.Fixed(2)) {
                        items(placesList) { place ->
                            ItemPlace(place = place) { fsqId ->
                                navController.navigate("item_detail_screen/$fsqId")
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
                textAlign = TextAlign.Start,
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
    var selectedText by remember { mutableStateOf("Order by") }
    var isOpenNow by remember { mutableStateOf(false) }

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
                .background(
                    color = colorResource(id = R.color.background_button_dark),
                    shape = RoundedCornerShape(25.dp)
                )
                .padding(4.dp)
        ) {
            Text(
                text = selectedText,
                style = MaterialTheme.typography.caption,
                color = colorResource(id = R.color.primary_text_color_light),
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
                Text(text = stringResource(id = R.string.cheap))
            }
            DropdownMenuItem(onClick = {
                onPriceOptionSelected("$$")
                selectedText = "Moderate"
                isMenuExpanded = false
            }) {
                Text(text = stringResource(id = R.string.moderate))
            }
            DropdownMenuItem(onClick = {
                onPriceOptionSelected("$$$")
                selectedText = "Expensive"
                isMenuExpanded = false
            }) {
                Text(text = stringResource(id = R.string.expensive))
            }
            DropdownMenuItem(onClick = {
                onPriceOptionSelected("$$$$")
                selectedText = "Very Expensive"
                isMenuExpanded = false
            }) {
                Text(text = stringResource(id = R.string.very_expensive))
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isOpenNow) stringResource(id = R.string.open) else stringResource(id = R.string.closed),
                color = colorResource(id = R.color.primary_text_color_dark),
                style = MaterialTheme.typography.caption,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
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
        border = null,
        modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .height(250.dp)
                .background(colorResource(id = R.color.background_card_color))
                .clickable { onItemClick(place.fsq_id) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .background(colorResource(id = R.color.background_card_top_dark)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                place.photos
                    ?.firstOrNull { "${it.prefix}${it.suffix}".isNotEmpty() }
                    ?.let { photo ->
                        val photoUrl = "${photo.prefix}1024${photo.suffix}".fixImageUrl()
                        PlaceRoundImage(url = photoUrl, Modifier.size(100.dp))
                    }
            }
            Spacer(modifier = Modifier.height(26.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = place.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6,
                    color = colorResource(id = R.color.primary_text_color_dark),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = place.distance,
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.secondary_text_color_dark),
                    )
                    Text(
                        text = place.price,
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.secondary_text_color_dark),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint =colorResource(id = R.color.secondary_text_color_dark),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = place.rating,
                        style = MaterialTheme.typography.body1,
                        color = colorResource(id = R.color.secondary_text_color_dark),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                }
            }
        }
    }
}