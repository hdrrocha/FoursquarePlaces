package com.example.foursquareplaces.ui.searchplaces.view


import android.annotation.SuppressLint

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.sp
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.foursquareplaces.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchPlacesView( navController: NavController) {


    Scaffold(
        topBar = {
            Toolbar()
        },
        backgroundColor = colorResource(id = R.color.background_color),
        content = {

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