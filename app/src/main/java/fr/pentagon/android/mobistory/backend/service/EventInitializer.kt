package fr.pentagon.android.mobistory.backend.service

import android.content.Context
import android.net.Uri
import android.util.Log
import fr.pentagon.android.mobistory.R
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.entity.Alias
import fr.pentagon.android.mobistory.backend.entity.Coordinate
import fr.pentagon.android.mobistory.backend.entity.Country
import fr.pentagon.android.mobistory.backend.entity.CountryEventJoin
import fr.pentagon.android.mobistory.backend.entity.Event
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
import fr.pentagon.android.mobistory.backend.json.EventDTO
import fr.pentagon.android.mobistory.backend.json.LanguageReferenceDTO
import fr.pentagon.android.mobistory.backend.json.loadJSONFile
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


fun LanguageReferenceDTO<String>?.representation(): String? {
    if (this == null) return null
    return (this.fr ?: "").plus("||").plus(this.en ?: "")
}

fun String?.toDate(): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return if (this == null) null else dateFormat.parse(this)
}

suspend fun eventInitializer(
    context: Context,
    onInserting: (Float) -> Unit,
    onFinish: () -> Unit
) {
    val content = context.loadJSONFile(R.raw.events) ?: throw AssertionError()
    val rawEvents = Json.decodeFromString<List<EventDTO>>(content)
    Log.i("INITIALIZER", rawEvents[0].toString())
    Log.i("NUMBER OF EVENTS TO LOAD:", "${rawEvents.size}")

    val start = System.currentTimeMillis();

    val events = mutableListOf<Event>()
    val aliases = mutableListOf<Alias>()
    val coordinates = mutableListOf<Coordinate>()
    val keyDates = mutableListOf<KeyDate>()

    val types = mutableMapOf<String, Type>()
    val eventTypeJoins = mutableListOf<EventTypeJoin>()

    val locations = mutableMapOf<String, Location>()
    val eventLocationJoins = mutableListOf<EventLocationJoin>()

    val countries = mutableMapOf<String, Country>()
    val eventCountryJoins = mutableListOf<CountryEventJoin>()

    val participants = mutableMapOf<String, Participant>()
    val eventParticipantJoins = mutableListOf<EventParticipantJoin>()

    val keywords = mutableMapOf<String, Keyword>()
    val eventKeywordJoins = mutableListOf<KeywordEventJoin>()

    val images = mutableListOf<Image>()

    // Might have to do it by batch if the file was too big
    rawEvents.forEachIndexed { index, event ->
        // Events
        events.add(
            Event(
                eventId = event.id,
                label = event.label.representation()!!,
                startDate = event.startDate.toDate(),
                endDate = event.endDate.toDate(),
                description = event.description.representation(),
                wikipedia = event.wikipedia.representation(),
                popularity = event.popularity?.fr ?: 0,
            )
        )

        // Aliases
        for (alias in event.aliases.fr) {
            aliases.add(Alias(label = alias, eventId = event.id))
        }
        for (alias in event.aliases.en) {
            aliases.add(Alias(label = alias, eventId = event.id))
        }

        // Coordinates
        for (coordinate in event.coords) {
            val splitted = coordinate.split(",").map { it.toDouble() }
            coordinates.add(Coordinate(splitted.first(), splitted.last(), event.id))
        }

        // Keydates
        for (keyDate in event.dates) {
            val date = keyDate.toDate()
            keyDates.add(KeyDate(date = date!!, eventId = event.id))
        }


        // Types
        for (type in event.type.fr) {
            eventTypeJoins.add(
                EventTypeJoin(
                    eventId = event.id,
                    typeId = types.getOrPut(type) {
                        val typeId = UUID.randomUUID()
                        Type(typeId = typeId, label = type)
                    }.typeId
                )
            )
        }
        for (type in event.type.en) {
            eventTypeJoins.add(
                EventTypeJoin(
                    eventId = event.id,
                    typeId = types.getOrPut(type) {
                        val typeId = UUID.randomUUID()
                        Type(typeId = typeId, label = type)
                    }.typeId
                )
            )
        }

        // Locations
        for (location in event.locations.fr) {
            eventLocationJoins.add(
                EventLocationJoin(
                    eventId = event.id,
                    locationId = locations.getOrPut(location) {
                        val typeId = UUID.randomUUID()
                        Location(locationId = typeId, location = location)
                    }.locationId
                )
            )
        }
        for (location in event.locations.en) {
            eventLocationJoins.add(
                EventLocationJoin(
                    eventId = event.id,
                    locationId = locations.getOrPut(location) {
                        val typeId = UUID.randomUUID()
                        Location(locationId = typeId, location = location)
                    }.locationId
                )
            )
        }

        // Countries
        for (country in event.countries.fr) {
            eventCountryJoins.add(
                CountryEventJoin(
                    eventId = event.id,
                    countryId = countries.getOrPut(country) {
                        val typeId = UUID.randomUUID()
                        Country(countryId = typeId, label = country)
                    }.countryId
                )
            )
        }
        for (country in event.countries.en) {
            eventCountryJoins.add(
                CountryEventJoin(
                    eventId = event.id,
                    countryId = countries.getOrPut(country) {
                        val typeId = UUID.randomUUID()
                        Country(countryId = typeId, label = country)
                    }.countryId
                )
            )
        }

        // Participants
        for (participant in event.participants.fr) {
            eventParticipantJoins.add(
                EventParticipantJoin(
                    eventId = event.id,
                    participantId = participants.getOrPut(participant) {
                        val typeId = UUID.randomUUID()
                        Participant(participantId = typeId, name = participant)
                    }.participantId
                )
            )
        }
        for (participant in event.participants.en) {
            eventParticipantJoins.add(
                EventParticipantJoin(
                    eventId = event.id,
                    participantId = participants.getOrPut(participant) {
                        val typeId = UUID.randomUUID()
                        Participant(participantId = typeId, name = participant)
                    }.participantId
                )
            )
        }

        // Keywords
        for (keyword in event.keywords.fr) {
            eventKeywordJoins.add(
                KeywordEventJoin(
                    eventId = event.id,
                    keywordId = keywords.getOrPut(keyword) {
                        val typeId = UUID.randomUUID()
                        Keyword(keywordId = typeId, label = keyword)
                    }.keywordId
                )
            )
        }
        for (keyword in event.keywords.en) {
            eventKeywordJoins.add(
                KeywordEventJoin(
                    eventId = event.id,
                    keywordId = keywords.getOrPut(keyword) {
                        val typeId = UUID.randomUUID()
                        Keyword(keywordId = typeId, label = keyword)
                    }.keywordId
                )
            )
        }

        // Images
        for (image in event.images) {
            images.add(Image(eventId = event.id, link = Uri.parse(image)))
        }

        onInserting(30f * (index.toFloat() / rawEvents.size))
    }


    Database.eventDao().saveAll(events)
    Database.aliasDao().saveAll(aliases)
    Database.coordinateDao().saveAll(coordinates)
    Database.keyDateDao().saveAll(keyDates)
    onInserting(40f)

    Database.typeDao().saveAll(types.values)
    Database.eventTypeJoinDao().saveAll(eventTypeJoins)
    onInserting(50f)

    Database.locationDao().saveAll(locations.values)
    Database.eventLocationJoinDao().saveAll(eventLocationJoins)
    onInserting(60f)

    Database.countryDao().saveAll(countries.values)
    Database.eventCountryJoinDao().saveAll(eventCountryJoins)
    onInserting(70f)

    Database.participantDao().saveAll(participants.values)
    Database.eventParticipantJoinDao().saveAll(eventParticipantJoins)
    onInserting(80f)

    Database.keywordDao().saveAll(keywords.values)
    Database.keywordEventJoinDao().saveAll(eventKeywordJoins)
    onInserting(90f)

    Database.imageDao().saveAll(images)
    onInserting(100f)

    val end = System.currentTimeMillis();
    Log.i("INSERTION COMPLETE IN:", "${(end - start)}ms")

    onFinish()
}