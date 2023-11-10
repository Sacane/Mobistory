package fr.pentagon.android.mobistory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobistoryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Mobistory()
                }
            }
        }
    }
}

@Composable
fun Mobistory(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            TopBar()
        }
        Box(
            modifier = Modifier
                .weight(8f)
                .background(color = Color.Yellow)
                .fillMaxSize()
        ) {
            // TODO ROUTING
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .background(color = Color.Green)
                .fillMaxSize()
        ) {
            BottomBar()
        }
    }
}
