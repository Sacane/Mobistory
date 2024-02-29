package fr.pentagon.android.mobistory.frontend.component

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import fr.pentagon.android.mobistory.SmallEventComponent
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun FavoritePage() {
    val events = remember {
        mutableStateListOf<Event>()
    }
    val state = rememberScrollState()
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            Database.favoriteDao().getAllFavorite().apply {
                events.addAll(this)
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TitledContent("Mes évènements favoris") {
            if(events.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(state)
                ) {
                    items(events.size) {
                        SmallEventComponent(
                            events[it].getFrenchLabel(),
                            events[it].description ?: "Pas de description disponible",
                            events[it].getFormatStartDate()
                        )
                    }
                }
            } else {
                Text("Vous n'avez pas encore d'évènement favoris")
            }
        }
    }

}

@Preview
@Composable
fun FavoritePreview() {
    Database.open(LocalContext.current)
    FavoritePage()
}