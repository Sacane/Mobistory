package fr.pentagon.android.mobistory.backend.json

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import fr.pentagon.android.mobistory.R
import kotlinx.serialization.json.Json


@Composable
fun EventInitializer(context: Context) {
    val content = context.loadJSONFile(R.raw.events) ?: throw AssertionError()
    val events = Json.decodeFromString<List<EventDTO>>(content).let {
        Log.i("INITIALIZER", it[0].toString())
    }
}