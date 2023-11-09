package fr.pentagon.android.mobistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import java.util.Date


data class Event(val name: String, val description: String, val date: Date)

@Composable
fun PrintText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MobistoryTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .weight(0.1f)
                .background(Color.Red)
                .fillMaxWidth()){
                PrintText("Title")
            }

            DisplayEventList(list = listOf(), modifier = Modifier)

            Box(modifier = Modifier
                .weight(0.1f)
                .background(Color.Green)
                .fillMaxWidth()){
                PrintText("Nav Zone")
            }
        }
    }
}


@Composable
fun DisplayEventList(list: List<Event>, modifier: Modifier){
    Row(modifier = modifier.fillMaxSize(0.8f)) {

    }
}

