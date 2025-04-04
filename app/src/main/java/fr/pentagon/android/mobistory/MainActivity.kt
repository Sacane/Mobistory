package fr.pentagon.android.mobistory

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.entity.AppVersion
import fr.pentagon.android.mobistory.backend.service.eventInitializer
import fr.pentagon.android.mobistory.frontend.component.BottomBar
import fr.pentagon.android.mobistory.frontend.component.FavoritePage
import fr.pentagon.android.mobistory.frontend.component.HomePage
import fr.pentagon.android.mobistory.frontend.component.LoadingScreen
import fr.pentagon.android.mobistory.frontend.component.Quiz
import fr.pentagon.android.mobistory.frontend.component.Search
import fr.pentagon.android.mobistory.frontend.component.TopBar
import fr.pentagon.android.mobistory.frontend.component.findContentPageFromUrl
import fr.pentagon.android.mobistory.frontend.service.LocationService
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Database.isInitialized) {
            Database.open(this)
        }
        setContent {
            MobistoryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var percentage by remember { mutableFloatStateOf(0f) }
                    var loaded by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        findContentPageFromUrl(
                            this@MainActivity,
                            "https://fr.wikipedia.org/wiki/Traité de Paris (1763)"
                        )
                        val versionDao = Database.appVersionDao()
                        val version = versionDao.getVersion()
                        if (version == null) {
                            versionDao.save(AppVersion(version = "1.0"))
                            eventInitializer(this@MainActivity, { p -> percentage = p }) {
                                Log.i("DATABASE", "Data insertion complete")
                                loaded = true
                            }
                        } else {
                            Log.i("database", "Database is already initialized")
                            loaded = true
                        }
                    }
                    if (loaded) Mobistory() else LoadingScreen(percentage = percentage)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Mobistory(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val permissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            TopBar()
        }
        NavHost(
            modifier = Modifier
                .weight(8f)
                .fillMaxSize(), navController = navController, startDestination = "home"
        ) {
            composable("home") {
                HomePage()
            }
            composable("search") {
                Search()
            }
            composable("favorites") {
                FavoritePage()
            }
            composable("quiz") {
                Quiz()
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .background(color = Color.Green)
                .fillMaxSize()
        ) {
            BottomBar(navController = navController)
        }
    }

    LaunchedEffect(permissions.allPermissionsGranted) {
        if (!permissions.allPermissionsGranted) {
            permissions.launchMultiplePermissionRequest()
        } else {
            val intent = Intent(context, LocationService::class.java).apply {
                action = "START"
            }
            context.startService(intent)
        }
    }
}
