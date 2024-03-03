package fr.pentagon.android.mobistory

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.ui.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import fr.pentagon.android.mobistory.backend.getEventOfTheDayId
import fr.pentagon.android.mobistory.frontend.component.EventDetail
import fr.pentagon.android.mobistory.frontend.component.EventDetailPreview
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

@Composable
fun Home() {
    val context = LocalContext.current
    var event by remember { mutableStateOf<Event?>(null)}

    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            val eotdId = getEventOfTheDayId(context)
            event = Database.eventDao().findById(eotdId)
        }
    }
    HomeContainer(event = event)
}

@Composable
fun HomeContainer(event: Event?) {
    event?.let { EventOfTheDayContainer(event = it) }
        ?: Text(text = "No event", modifier = Modifier.padding(8.dp))
}

@Composable
fun EventOfTheDayContainer(event: Event) {
    var showDetails by remember { mutableStateOf(false) }
    if (showDetails) {
        EventDetail(context = LocalContext.current, event = event)
    } else {
        EventOfTheDayDisplayer(event = event, modifier = Modifier.clickable { showDetails = true })
    }
}

@Composable
fun EventOfTheDayDisplayer(event: Event, modifier: Modifier) {
    Column(modifier = modifier
        .padding(8.dp)) {
        Text(
            text = "Évènement du jour",
            style = Typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )
        Divider()
        Column (modifier = Modifier.padding(8.dp)) {
            Text(text = event.title, style = Typography.headlineMedium)
            EventOfTheDayDateDisplayer(event = event)
            Spacer(modifier = Modifier.size(8.dp))
            Log.i("DEBUG", event?.description ?: "que dalle")
            event.brief?.let { Text(text = it, style = Typography.bodyMedium) }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Cliquez pour en savoir plus", style = Typography.headlineSmall)
        }
    }
}

@Composable
fun EventOfTheDayDateDisplayer(event: Event) {
    event.startDate?.let {
        Spacer(modifier = Modifier.size(8.dp))
        Row {
            Text(text = SimpleDateFormat("dd/MM/yyyy").format(it), style = Typography.bodyMedium)
            event.endDate?.let {
                Text(text = " - " + SimpleDateFormat("dd/MM/yyyy").format(it), style = Typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventOfTheDayPreview() {
    val event = Event(label = "Révolution française||French Revolution",
        startDate = Date.from(Instant.parse("1789-07-14T12:00:00Z")),
        endDate = Date.from(Instant.parse("1799-11-09T12:00:00Z")),
        description = "période de l'histoire de France et de ses colonies, entre le 5 mai 1789 et le 9 novembre 1799||revolution in France from 1789 to 1799"
        )
    HomeContainer(event = event)
}