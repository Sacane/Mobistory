package fr.pentagon.android.mobistory.backend.json

import android.content.Context
import android.util.Log
import fr.pentagon.android.mobistory.R
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.entity.Alias
import fr.pentagon.android.mobistory.backend.entity.Coordinate
import fr.pentagon.android.mobistory.backend.entity.KeyDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



fun LanguageReferenceDTO<String>?.representation(): String? {
    if(this == null) return null
    return (this.fr ?: "").plus("||").plus(this.en ?: "")
}
fun LanguageReferenceListDTO<String>.representation(): String {
    return ""
}

fun String?.toDate(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return if(this == null) null else dateFormat.parse(this)
}

suspend fun eventInitializer(context: Context, onFinish: () -> Unit) {
    val content = context.loadJSONFile(R.raw.events) ?: throw AssertionError()
    val events = Json.decodeFromString<List<EventDTO>>(content)
    Log.i("INITIALIZER", events[0].toString())

    withContext(Dispatchers.IO) {
        val aliasesDao = Database.aliasDao()
        val eventDao = Database.eventDao()
        val coordinateDao = Database.coordinateDao()
        val keyDateDao = Database.keyDateDao()
        for(event in events) {
            if(eventDao.existsByLabel(event.label.representation()!!)){
                continue
            }
            val toInsert = Event(
                eventId = event.id,
                label = event.label.representation()!!,
                startDate = event.startDate.toDate(),
                endDate = event.endDate.toDate(),
                description = event.description.representation(),
                wikipedia = event.wikipedia.representation()
            )
            eventDao.save(toInsert)
            val sized = if (event.aliases.fr.size > event.aliases.en.size) event.aliases.fr.size else event.aliases.en.size
            for(i in 0 until sized){
                aliasesDao.insertAlias(Alias(label = event.aliases.fr[i].plus("||").plus(event.aliases.en[i]), eventId = toInsert.eventId))
            }
            for(coordinate in event.coords) {
                coordinateDao.save(Coordinate(value = coordinate, eventId = toInsert.eventId))
            }
            for(keyDate in event.dates) {
                val date = keyDate.toDate()
                keyDateDao.save(KeyDate(date = date!!, eventId = event.id))
            }
        }
    }
    onFinish()
}