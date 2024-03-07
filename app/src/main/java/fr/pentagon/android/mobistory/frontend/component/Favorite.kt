package fr.pentagon.android.mobistory.frontend.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.pentagon.android.mobistory.SmallEventComponent
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.entity.FavoriteEvent
import fr.pentagon.android.mobistory.backend.entity.FavoriteEventAndEvent
import fr.pentagon.android.mobistory.ui.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(events.size) {
                        SmallEventComponent(
                            event = events[it].event,
                            label = events[it].favoriteEvent.customLabel,
                            onClick = {}
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
fun FavoriteButton(event: Event, buttonSize: Dp) {
    val scope = rememberCoroutineScope()
    var isFavorited by rememberSaveable { mutableStateOf(false) }
    var openAlertDialog by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(event) {
        isFavorited = Database.favoriteDao().isFavorited(event.eventId)
    }

    IconButton(
        onClick = {
            if (!isFavorited) {
                openAlertDialog = true
            } else {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        Database.favoriteDao().deleteFavorite(event.eventId)
                    }
                    isFavorited = false
                }
            }
        },
        modifier = Modifier.size(buttonSize)
    ) {
        Icon(
            if (isFavorited) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            tint = Color.Magenta,
            modifier = Modifier.size(buttonSize)
        )
    }

    when {
        openAlertDialog -> FavoriteDialogConfirm(
            onDismissRequest = { openAlertDialog = false },
            onConfirmation = { label ->
                openAlertDialog = false
                isFavorited = true
                scope.launch {
                    Database.favoriteDao().addFavorite(
                        FavoriteEvent(
                            favoriteEventId = event.eventId,
                            customLabel = label
                        )
                    )
                }
            }
        )
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