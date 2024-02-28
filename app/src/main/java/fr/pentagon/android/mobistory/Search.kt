package fr.pentagon.android.mobistory

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(modifier: Modifier = Modifier) {
    var searchedText by remember { mutableStateOf(TextFieldValue()) }
    var listOfEv by remember { mutableStateOf(emptyList<Event>()) }
    var visible by remember { mutableStateOf(true) }
    var selectedOrder by remember { mutableStateOf(SortOrder.INIT) }
    var dateState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Input, yearRange = (-5000..3000))

    LaunchedEffect(searchedText.text) {
        if (searchedText.text.isNullOrBlank()) {
            listOfEv = withContext(Dispatchers.IO) {
                when(selectedOrder) {
                    SortOrder.INIT -> Database.eventDao().getEventsLimitOf50()
                    SortOrder.DATE_ASC -> Database.eventDao().findEventsOrderedAscendingDateLimit50()
                    SortOrder.DATE_DESC -> Database.eventDao().findEventsOrderedDescendingDateLimit50()
                    SortOrder.POPULARITY -> listOf()//TODO FILTER POPULARITY
                }
            }
        } else {
            listOfEv = withContext(Dispatchers.IO) {
                Database.eventDao().getEventsContainsSearchQuery(searchedText.text)
            }
            when(selectedOrder) {
                SortOrder.INIT -> Database.eventDao().getEventsLimitOf50()
                SortOrder.DATE_ASC -> Database.eventDao().findEventsOrderedAscendingDateLimit50()
                SortOrder.DATE_DESC -> Database.eventDao().findEventsOrderedDescendingDateLimit50()
                SortOrder.POPULARITY -> listOf()//TODO FILTER POPULARITY
            }
        }
    }
    Column {
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(0.1f)){
            SearchBarComponent(onSearch = { it -> searchedText = it }, onActiveFilter = {visible = !visible})
        }
        if(visible){
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(0.8f)){
                FilterComponent(onSelectedSortOrder = {it -> selectedOrder = it}, onSelectedDateInterval = {it -> dateState = it})
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        var listWeight = if (visible) 0.2f else 0.9f
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(listWeight)){
            DisplaySmallEventsList(events = listOfEv, modifier = modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    MobistoryTheme {
        Search()
    }
}