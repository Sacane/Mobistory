package fr.pentagon.android.mobistory.backend.json

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.util.Log
import fr.pentagon.android.mobistory.R
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.entity.Alias
import fr.pentagon.android.mobistory.backend.entity.AliasDao
import fr.pentagon.android.mobistory.backend.entity.Coordinate
import fr.pentagon.android.mobistory.backend.entity.Country
import fr.pentagon.android.mobistory.backend.entity.CountryEventJoin
import fr.pentagon.android.mobistory.backend.entity.EventLocationJoin
import fr.pentagon.android.mobistory.backend.entity.EventParticipantJoin
import fr.pentagon.android.mobistory.backend.entity.EventTypeJoin
import fr.pentagon.android.mobistory.backend.entity.Image
import fr.pentagon.android.mobistory.backend.entity.KeyDate
import fr.pentagon.android.mobistory.backend.entity.Keyword
import fr.pentagon.android.mobistory.backend.entity.KeywordEventJoin
import fr.pentagon.android.mobistory.backend.entity.Location
import fr.pentagon.android.mobistory.backend.entity.Participant
import fr.pentagon.android.mobistory.backend.entity.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


fun LanguageReferenceDTO<String>?.representation(): String? {
    if(this == null) return null
    return (this.fr ?: "").plus("||").plus(this.en ?: "")
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
        val typeDao = Database.typeDao()
        val typeJoinDao = Database.eventTypeJoinDao()
        val locationDao = Database.locationDao()
        val locationEventDao = Database.eventLocationJoinDao()
        val countryDao = Database.countryDao()
        val eventCountryJoinDao = Database.eventCountryJoinDao()
        val participantDao = Database.participantDao()
        val eventParticipantJoinDao = Database.eventParticipantJoinDao()
        val imageDao = Database.imageDao()
        val keywordDao = Database.keywordDao()
        val keywordEventJoinDao = Database.keywordEventJoinDao()
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
                wikipedia = event.wikipedia.representation(),
                popularity = event.popularity?.fr ?: 0
            )
            eventDao.save(toInsert)
            for(alias in event.aliases.fr) {
                aliasesDao.insertAlias(Alias(label = alias, eventId = event.id))
            }
            for(alias in event.aliases.en) {
                aliasesDao.insertAlias(Alias(label = alias, eventId = event.id))
            }
            for(coordinate in event.coords) {
                coordinateDao.save(Coordinate(value = coordinate, eventId = toInsert.eventId))
            }
            for(keyDate in event.dates) {
                val date = keyDate.toDate()
                keyDateDao.save(KeyDate(date = date!!, eventId = event.id))
            }
            for(type in event.type.fr) {
                val typeRegistered = typeDao.findByLabel(type)
                if(typeRegistered == null) {
                    val typeId = UUID.randomUUID()
                    val created = Type(typeId = typeId, label = type)
                    typeDao.save(created)
                    typeJoinDao.save(EventTypeJoin(eventId = event.id, typeId = typeId))
                }else {
                    try {
                        typeJoinDao.save(
                            EventTypeJoin(
                                eventId = event.id,
                                typeId = typeRegistered.typeId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
            for(type in event.type.en) {
                val typeRegistered = typeDao.findByLabel(type)
                if(typeRegistered == null) {
                    val created = Type(label = type)
                    typeDao.save(created)
                }else {
                    try {
                        typeJoinDao.save(
                            EventTypeJoin(
                                eventId = event.id,
                                typeId = typeRegistered.typeId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
            for(location in event.locations.fr) {
                val locationRegistered = locationDao.findByLabel(location)
                if(locationRegistered == null) {
                    locationDao.save(Location(location = location))
                } else {
                    try {
                        locationEventDao.save(
                            EventLocationJoin(
                                eventId = event.id,
                                locationId = locationRegistered.locationId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
            for(location in event.locations.en) {
                val locationRegistered = locationDao.findByLabel(location)
                if(locationRegistered == null) {
                    locationDao.save(Location(location = location))
                } else {
                    try {
                        locationEventDao.save(
                            EventLocationJoin(
                                eventId = event.id,
                                locationId = locationRegistered.locationId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
            for(country in event.countries.fr) {
                val countryRegistered = countryDao.findByLabel(country)
                if(countryRegistered == null) {
                    val countryId = UUID.randomUUID()
                    countryDao.save(Country(countryId = countryId, label = country))
                    eventCountryJoinDao.insert(CountryEventJoin(event.id, countryId))
                } else {
                    try {
                        eventCountryJoinDao.insert(
                            CountryEventJoin(
                                event.id,
                                countryRegistered.countryId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
            for(country in event.countries.en) {
                val countryRegistered = countryDao.findByLabel(country)
                if(countryRegistered == null) {
                    val countryId = UUID.randomUUID()
                    countryDao.save(Country(countryId = countryId, label = country))
                    eventCountryJoinDao.insert(CountryEventJoin(event.id, countryId))
                } else {
                    try {
                        eventCountryJoinDao.insert(
                            CountryEventJoin(
                                event.id,
                                countryRegistered.countryId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
            for(participant in event.participants.fr) {
                val participantRegistered = participantDao.findByLabel(participant)
                if(participantRegistered == null) {
                    val participantId = UUID.randomUUID()
                    participantDao.save(Participant(participantId = participantId, name = participant))
                    eventParticipantJoinDao.save(EventParticipantJoin(event.id, participantId))
                } else {
                    try {
                        eventParticipantJoinDao.save(
                            EventParticipantJoin(
                                event.id,
                                participantRegistered.participantId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
            for(participant in event.participants.en) {
                val participantRegistered = participantDao.findByLabel(participant)
                if(participantRegistered == null) {
                    val participantId = UUID.randomUUID()
                    participantDao.save(Participant(participantId = participantId, name = participant))
                    eventParticipantJoinDao.save(EventParticipantJoin(event.id, participantId))
                } else {
                    try {
                        eventParticipantJoinDao.save(
                            EventParticipantJoin(
                                event.id,
                                participantRegistered.participantId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
            for(image in event.images) {
                imageDao.insertImage(Image(eventId = event.id, link = Uri.parse(image)))
            }
            for(keyword in event.keywords.fr) {
                Log.i("keyword track fr ", "keyword :$keyword and eventId : ${event.id}")
                val keywordRegistered = keywordDao.findByLabel(keyword)
                if(keywordRegistered == null) {
                    val keywordId = UUID.randomUUID()
                    keywordDao.save(Keyword(keywordId = keywordId, label = keyword))
                    keywordEventJoinDao.insert(KeywordEventJoin(eventId = event.id, keywordId = keywordId))
                } else {
                    try {
                        keywordEventJoinDao.insert(
                            KeywordEventJoin(
                                eventId = event.id,
                                keywordId = keywordRegistered.keywordId
                            )
                        )
                    }catch (_: Exception) {}
                }
            }
            for(keyword in event.keywords.en) {
                Log.i("keyword track en ", "keyword :$keyword and eventId : ${event.id}")
                val keywordRegistered = keywordDao.findByLabel(keyword)
                if(keywordRegistered == null) {
                    val keywordId = UUID.randomUUID()
                    keywordDao.save(Keyword(keywordId = keywordId, label = keyword))
                    keywordEventJoinDao.insert(KeywordEventJoin(eventId = event.id, keywordId = keywordId))
                } else {
                    try {
                        keywordEventJoinDao.insert(
                            KeywordEventJoin(
                                eventId = event.id,
                                keywordId = keywordRegistered.keywordId
                            )
                        )
                    }catch (_: SQLiteConstraintException) {

                    }
                }
            }
        }
    }
    onFinish()
}