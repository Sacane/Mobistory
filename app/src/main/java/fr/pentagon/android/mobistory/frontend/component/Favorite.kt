package fr.pentagon.android.mobistory.frontend.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.pentagon.android.mobistory.SmallEventComponent
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.entity.FavoriteEventAndEvent
import fr.pentagon.android.mobistory.ui.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class FavoriteElement(val event: Event, val label: String)

@Composable
fun FavoritePage() {
    val events = remember {
        mutableStateListOf<FavoriteEventAndEvent>()
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            Database.favoriteDao().getAllFavorite().apply {
                events.addAll(this)
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TitledContent("Favoris") {
            if (events.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(events.size) {
                        SmallEventComponent(
                            event = events[it].event,
                            label = events[it].favoriteEvent.customLabel
                        )
                    }
                }
            } else {
                Text("Vous n'avez pas encore d'évènement favoris")
            }
        }
    }

}

@Composable
fun FavoriteDialogConfirm(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    var label by remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Ajout en favori",
                    style = Typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Indiquer l'étiquette à attacher à ce favori",
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                TextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Étiquette") },
                    maxLines = 1,
                    modifier = Modifier.padding(16.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = true,
                        imeAction = ImeAction.Done
                    )
                )
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = "Annuler")
                    }
                    TextButton(onClick = { onConfirmation(label) }) {
                        Text(text = "Confirmer")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePreview() {
    Database.open(LocalContext.current)
    FavoritePage()
}