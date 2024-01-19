package fr.pentagon.android.mobistory.backend.json

import android.content.Context
import android.util.Log
import fr.pentagon.android.mobistory.R
import fr.pentagon.android.mobistory.backend.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


suspend fun eventInitializer(context: Context) {
    val content = context.loadJSONFile(R.raw.events) ?: throw AssertionError()
    val events = Json.decodeFromString<List<EventDTO>>(content)
    Log.i("INITIALIZER", events[0].toString())
    withContext(Dispatchers.IO) {
        val imageDao = Database.imageDao()
    }
}