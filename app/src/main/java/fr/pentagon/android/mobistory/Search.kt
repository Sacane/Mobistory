package fr.pentagon.android.mobistory

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.frontend.component.EventDetail
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(modifier: Modifier = Modifier) {
    var searchedText by remember { mutableStateOf(TextFieldValue()) }
    var listOfEv by remember { mutableStateOf(emptyList<Event>()) }
    var visible by remember { mutableStateOf(false) }
    var selectedOrder by remember { mutableStateOf(SortOrder.INIT) }
    var dateState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Input, yearRange = (-5000..3000))
    var searchKey by remember { mutableIntStateOf(0) }
    var eventDetail by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(searchedText, searchKey) {
        listOfEv = withContext(Dispatchers.IO) {
            when {
                searchedText.text.isBlank() -> {
                    when(selectedOrder) {
                        SortOrder.INIT -> Database.eventDao().findEventsLimitOf50()
                        SortOrder.DATE_ASC -> Database.eventDao().findEventsOrderedAscendingDateLimit50()
                        SortOrder.DATE_DESC -> Database.eventDao().findEventsOrderedDescendingDateLimit50()
                        SortOrder.POPULARITY -> Database.eventDao().findEventsOrderedByPopularityLimit50()
                    }
                }
                else -> {
                    when(selectedOrder) {
                        SortOrder.INIT -> Database.eventDao().findEventsContainsSearchQuery(searchedText.text)
                        SortOrder.DATE_ASC -> Database.eventDao().findEventsContainsSearchQueryOrderedAscendingDate(searchedText.text)
                        SortOrder.DATE_DESC -> Database.eventDao().findEventsContainsSearchQueryOrderedDescendingDate(searchedText.text)
                        SortOrder.POPULARITY -> Database.eventDao().findEventsContainsSearchOrderedByPopularity(searchedText.text)
                    }
                }
            }
        }
    }

    val filteredEvents = listOfEv.filter { event ->
        dateState.selectedStartDateMillis?.let { selectedStartDate ->
            (event.startDate?.time ?: 0) > selectedStartDate
        } ?: true

    } .filter { event ->
        dateState.selectedEndDateMillis?.let { selectedEndDate ->
            (event.endDate?.time ?: Long.MAX_VALUE) < selectedEndDate
        } ?: true
    }
    if(eventDetail == null) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.1f)
            ) {
                SearchBarComponent(onSearch = { searchedText = it },
                    onActiveFilter = { visible = !visible })
            }
            if (visible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.8f)
                ) {
                    FilterComponent(
                        onSelectedSortOrder = { selectedOrder = it },
                        onSelectedDateInterval = { dateState = it },
                        selectedSort = selectedOrder,
                        dateState = dateState
                    )
                }
            }
            Divider(modifier = Modifier.padding(8.dp))
            val listWeight = if (visible) 0.2f else 0.9f
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(listWeight)
            ) {
                DisplaySmallEventsList(events = filteredEvents, modifier = modifier) {
                    eventDetail = it
                }
            }
        }
        DisposableEffect(selectedOrder) {
            searchKey++
            onDispose { }
        }
    }else {
        EventDetail(context = LocalContext.current, event = eventDetail!!)
    }
}

/*@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    MobistoryTheme {
        Search()
    }
}*/