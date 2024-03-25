package fr.pentagon.android.mobistory


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.frontend.component.FavoriteButton


data class FakeEvent(val name: String, val description: String, val date: String)

/**
 * Represent on small event component. (ex : During a research)
 *
 * @param event
 * @param label
 */
@Composable
fun SmallEventComponent(event: Event, label: String? = null, onClick: (Event) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.hsv(36F, 0.17F, 1F)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            label?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify,
                    color = Color.Red
                )
            }
            Text(
                text = event.title,
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = event.getCleanDate(),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = event.brief,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { onClick(event) }, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.ArrowForward, contentDescription = "Regarder l'article",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                FavoriteButton(event = event, buttonSize = 32.dp)
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
fun DisplaySmallEventsList(events: List<Event>, modifier: Modifier, onClick: (Event) -> Unit = {_ -> }) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        items(events.size) { index ->
            val currentEv = events[index]
            SmallEventComponent(
                event = currentEv,
                onClick = onClick
            )
        }
    }
}

