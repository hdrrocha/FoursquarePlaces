package com.example.foursquareplaces

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foursquareplaces.ui.itemdetailscreen.view.ItemDetailScreen
import com.example.foursquareplaces.ui.itemdetailscreen.viewmodel.PlaceDetailsViewModel
import com.example.foursquareplaces.ui.searchplaces.view.SearchPlacesView
import com.example.foursquareplaces.ui.searchplaces.viewmodel.SearchPlacesViewModel
import com.example.foursquareplaces.ui.theme.FoursquarePlacesTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val searchPlacesViewModel: SearchPlacesViewModel = getViewModel()
            val placeDetailsViewModel: PlaceDetailsViewModel = getViewModel()

            FoursquarePlacesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "main_screen") {
                        composable("main_screen") {
                            SearchPlacesView(searchPlacesViewModel, navController = navController)
                        }
                        composable(
                            "item_detail_screen/{itemName}",
                            arguments = listOf(navArgument("itemName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val itemName = backStackEntry.arguments?.getString("itemName")
                            itemName?.let {
                                ItemDetailScreen(viewModel = placeDetailsViewModel, name = it, onBackPressed = { navController.popBackStack() })

                            }
                        }
                    }

                }
            }
        }
    }
}