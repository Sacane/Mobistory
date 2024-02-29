package fr.pentagon.android.mobistory

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class SortOrder(val value: String) {
    POPULARITY("Popularity"), DATE_ASC("Date ascending"), DATE_DESC("Date descending"), INIT("None")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterComponent(modifier: Modifier = Modifier, onSelectedSortOrder: (SortOrder) -> Unit, onSelectedDateInterval: (DateRangePickerState) -> Unit, selectedSort: SortOrder) {
    val sortOptions = listOf(SortOrder.POPULARITY, SortOrder.DATE_DESC, SortOrder.DATE_ASC)
    val dateState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Input, yearRange = (-5000..3000))

    Column(modifier = modifier.padding(8.dp)) {
        Divider()
        Text(text = "Filters", style = MaterialTheme.typography.headlineSmall)
        Divider()
        EventDatePickerComponent(dateState, onSelectedDateInterval)
        Divider()
        Text(text = "Sort", style = MaterialTheme.typography.headlineSmall)
        Divider()
        SortSelectComponent(sortOptions, selectedSort, onSelectedSortOrder)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDatePickerComponent(state: DateRangePickerState, onSelectedDateInterval: (DateRangePickerState) -> Unit) {
    Column (modifier = Modifier.padding(8.dp)) {
        Text(text = "Event Date",  style = MaterialTheme.typography.titleMedium)
        LaunchedEffect(state) {
            onSelectedDateInterval.invoke(state)
        }
        DateRangePicker(state = state, showModeToggle = false, title = {})
    }
}


@Composable
fun SortSelectComponent(sortOptions: List<SortOrder>, selectedOption: SortOrder, onSelectedSortOrder: (SortOrder) -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        sortOptions.forEach { sortOption ->
            if(sortOption != SortOrder.INIT) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedOption == sortOption,
                        onClick = { onSelectedSortOrder(sortOption) }
                    )
                    Text(
                        text = sortOption.value,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun FilterPreview() {
    var test = SortOrder.POPULARITY
    var test2 = rememberDateRangePickerState()
    FilterComponent(onSelectedSortOrder = {it -> test = it}, onSelectedDateInterval = {it -> test2 = it}, selectedSort = test)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun EventDatePickerPreview() {
    var dateState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Input)
    EventDatePickerComponent(dateState, onSelectedDateInterval = {it -> dateState = it})
}

@Preview(showBackground = true)
@Composable
fun SortSelectPreview() {
    val sortOptions = listOf(SortOrder.POPULARITY, SortOrder.DATE_DESC, SortOrder.DATE_ASC)
    var selectedSort by remember { mutableStateOf(sortOptions[0]) }
    SortSelectComponent(sortOptions, selectedSort) {
        selectedSort = it
    }
}