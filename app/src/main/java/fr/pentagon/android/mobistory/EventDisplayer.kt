package fr.pentagon.android.mobistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme


data class Event(val name: String, val description: String)

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
            Box(modifier = Modifier
                .weight(0.1f)
                .background(Color.Blue)
                .fillMaxWidth()){
                PrintText("SearchField")
            }

            DisplayEventList(evList = listOf(Event("test", "test"), Event("test", "test"),Event("test", "test"),Event("test", "test"),Event("test", "test"),Event("test", "test"),Event("test", "test"),Event("test", "test")), modifier = Modifier)

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
fun DisplayEventList(evList: List<Event>, modifier: Modifier){
    val scrollState = rememberLazyListState()
    LazyColumn(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(0.7f),
        state = scrollState
        )
    {
        items(evList.size) { index ->
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth()){
                Card(modifier = Modifier
                    .width(320.dp)
                    .height(120.dp)
                    .padding(8.dp)
                    .align(Alignment.Center),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Event $index")
                    Text(text = "Hello, world!")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

