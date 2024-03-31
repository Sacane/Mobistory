package fr.pentagon.android.mobistory.backend.service

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import fr.pentagon.android.mobistory.backend.Database
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "eotd")
data class EventOfTheDay(
    val id: Int,
    val lastUpdated: Long
)

val EOTD_ID = intPreferencesKey("eotd_id")
val LAST_UPDATED = longPreferencesKey("eotd_last_updated")

suspend fun readEventOfTheDay(context: Context): EventOfTheDay {
    val dataStore = context.dataStore.data.first()
    val id = dataStore[EOTD_ID] ?: 0
    val lastUpdated = dataStore[LAST_UPDATED] ?: 0
    return EventOfTheDay(id, lastUpdated)
}

suspend fun writeEventOfTheDay(context: Context, eventOfTheDay: EventOfTheDay) {
    context.dataStore.edit {
        it[EOTD_ID] = eventOfTheDay.id
        it[LAST_UPDATED] = eventOfTheDay.lastUpdated
    }
}

suspend fun getEventOfTheDayId(context: Context): Int {
    val cachedEventOfTheDay = readEventOfTheDay(context = context)
    val currentDay = timestampToLocalDate(System.currentTimeMillis())
    val lastUpdate = timestampToLocalDate(cachedEventOfTheDay.lastUpdated)

    if (isAnotherDay(currentDay, lastUpdate)) {
        val eventId = Database.eventDao()
            .findTop5EventByDay(currentDay.dayOfMonth.toFormatDate(), currentDay.monthValue.toFormatDate())
            .randomOrNull()?.eventId
            ?: Database.eventDao()
                .findTop5EventByMonth(currentDay.monthValue.toFormatDate())
                .randomOrNull()?.eventId
            ?: -1

        val newEventOfTheDay = EventOfTheDay(eventId, System.currentTimeMillis())
        writeEventOfTheDay(context = context, eventOfTheDay = newEventOfTheDay)
        return newEventOfTheDay.id
    }

    return cachedEventOfTheDay.id
}

fun Int.toFormatDate(): String {
    return if(this < 10) "0".plus(this.toString()) else this.toString()
}

fun isAnotherDay(date1: LocalDate, date2: LocalDate): Boolean {
    return date1.year != date2.year
            || date1.monthValue != date2.monthValue
            || date1.dayOfMonth != date2.dayOfMonth
}

fun timestampToLocalDate(timestamp: Long): LocalDate {
    return Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}