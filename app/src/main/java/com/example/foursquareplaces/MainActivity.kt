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
import com.example.foursquareplaces.utils.MyLocationManager
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {

    private lateinit var locationManager: MyLocationManager
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
                            "item_detail_screen/{fsqId}",
                            arguments = listOf(navArgument("fsqId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val fsqId = backStackEntry.arguments?.getString("fsqId")
                            fsqId?.let {
                                ItemDetailScreen(viewModel = placeDetailsViewModel, fsqId = it, onBackPressed = { navController.popBackStack() })
                            }
                        }
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        locationManager = MyLocationManager(this)
        locationManager.startLocationUpdates()
    }
    override fun onDestroy() {
        super.onDestroy()
        locationManager.stopLocationUpdates()

    }

}
