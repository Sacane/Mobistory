package fr.pentagon.android.mobistory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme


data class FakeEvent(val name: String, val description: String, val date: String)

/**
 * Represent on small event component. (ex : During a research)
 *
 * @param name
 * @param description
 * @param date
 */
@Composable
fun SmallEventComponent(name: String, description: String, date: String){
    Box(modifier = Modifier.fillMaxWidth()){
        Card(modifier = Modifier
            .width(320.dp)
            .height(120.dp)
            .padding(8.dp)
            .align(Alignment.Center)
            .clickable {
                //TODO Add actions when click
            },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = name,
                modifier = Modifier.padding(horizontal = 10.dp),
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Justify
            )
            Text(modifier = Modifier.padding(horizontal = 10.dp),
                text = date, fontSize = 8.sp,
                style =  TextStyle(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = description,
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 10.sp),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}


/**
 * Display list of small event.
 *
 * @param events list of events
 */
@Composable
fun DisplaySmallEventsList(events: List<Event>, modifier: Modifier){
    val scrollState = rememberLazyListState()
    LazyColumn(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(), //A modifier pour adapter la hauteur avec les autres éléments
        state = scrollState
        )
    {
        items(events.size) { index ->
            Spacer(modifier = Modifier.height(8.dp))
            val currentEv = events[index]

            SmallEventComponent(name = currentEv.getFrenchLabel(),
                description = currentEv.getFrenchDescription(),
                date = currentEv.getCleanDate()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

