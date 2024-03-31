package fr.pentagon.android.mobistory.frontend.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.entity.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NearbyEvents(location : Pair<Double, Double>) {
    val events = remember {
        mutableStateListOf<Event>()
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            Database.coordinateDao().nearbyEventFrom(location.first, location.second).apply {
                events.addAll(this)
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TitledContent("Nearby events") {
            if (events.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(events.size) {
                        SmallEventComponent(
                            event = events[it],
                            label = events[it].label,
                            onClick = {}
                        )
                    }
                }
            } else {
                Text("Aucun évènement à proximité")
            }
        }
    }
}