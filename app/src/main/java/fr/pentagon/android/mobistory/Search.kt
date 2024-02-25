package fr.pentagon.android.mobistory

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun Search(modifier: Modifier = Modifier) {
    var searchedText by remember { mutableStateOf(TextFieldValue()) }
    var listOfEv by remember { mutableStateOf(emptyList<Event>()) }

    LaunchedEffect(searchedText.text) {
        if (searchedText.text.isNullOrBlank()) {
            listOfEv = withContext(Dispatchers.IO) {
                Database.eventDao().getEventsLimitOf50()
            }
        } else {
            listOfEv = withContext(Dispatchers.IO) {
                Database.eventDao().getEventsContainsSearchQuery(searchedText.text)
            }
            Log.i("Size event after filtering list", "${listOfEv.size}")
        }
    }
    Column {
        SearchBarComponent(onSearch = { it -> searchedText = it })
        DisplaySmallEventsList(events = listOfEv, modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    MobistoryTheme {
        Search()
    }
}