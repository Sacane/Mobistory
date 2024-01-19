package fr.pentagon.android.mobistory.backend.entity

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.EventDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class EventTest{
    private lateinit var db: Database
    private lateinit var eventDao: EventDao
    @Before
    fun init() {
        val context: Context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context,
            Database::class.java
        ).build()
        eventDao = db.eventDao()
    }
    @Test
    fun simpleEventInsertTest() = runTest {
        val id = UUID.randomUUID()
        val toInsert = Event(eventId = id, label = "Hello", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "I don't know")
        eventDao.save(toInsert)
        val event = eventDao.findById(id)
        assertEquals(toInsert, event)
    }

    @Test
    fun shouldGetAllInsertedEvent() = runTest {
        val event1 = Event(label = "event1", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "randomUri")
        val event2 = Event(label = "event2", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "randomURI")
        val event3 = Event(label = "event3", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "randomURI")

        eventDao.save(event1)
        eventDao.save(event2)
        eventDao.save(event3)

        val events = eventDao.getAll()
        assertEquals(3, events.size)
        assertTrue(event1 in events)
        assertTrue(event2 in events)
        assertTrue(event3 in events)
    }

    @Test
    fun shouldGetAllKeyDatesWhenInserted() = runTest {
        val keyDateDao = db.keyDateDao()
        val dateStrings = listOf(
            "2023-01-01",
            "2023-02-15",
            "2023-03-30",
            "2023-04-10",
            "2023-05-25"
        )
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val event1 = Event(label = "event1", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "randomUri")
        eventDao.save(event1)
        dateStrings.map { dateFormat.parse(it) }.forEach { date -> if(date != null) keyDateDao.save(KeyDate(date = date, eventId = event1.eventId)) }

        val eventDates = eventDao.findEventWithKeyDateById(event1.eventId)
        assertEquals(5, eventDates.keyDates.size)
    }

    @Test
    fun shouldPersistCorrectlyImageFromEvent() = runTest {
        val imageDao = db.imageDao()
        val event1 = Event(label = "event1", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "randomUri")
        eventDao.save(event1)
        val image = Image(link = Uri.parse("http://localhost:8080/image.png"), eventId = event1.eventId)
        imageDao.insertImage(image)


        val images = eventDao.getImagesByEventId(event1.eventId)
        assertEquals(1, images.images.size)
        assertEquals(image, images.images.first())
    }

    @Test
    fun shouldPersistEventKeywords() = runTest {
        val event1 = Event(label = "event1", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "randomUri")
        eventDao.save(event1)
        val aliasDao = db.aliasDao()
        aliasDao.insertAlias(Alias(label = "ev1", eventId = event1.eventId))

        val aliases = eventDao.findEventWithAliasesById(event1.eventId)

        assertEquals(1, aliases.aliases.size)
        assertEquals("ev1", aliases.aliases.first().label)
    }

    @Test
    fun shouldPersistEventWithCoordinates() = runTest {
        val event1 = Event(label = "event1", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "randomUri")
        eventDao.save(event1)
        val aliasDao = db.coordinateDao()
        aliasDao.save(Coordinate(value = "ev1", eventId = event1.eventId))

        val coordinates = eventDao.findEventWithCoordinateById(event1.eventId)

        assertEquals(1, coordinates.coordinates.size)
        assertEquals("ev1", coordinates.coordinates.first().value)
    }
}