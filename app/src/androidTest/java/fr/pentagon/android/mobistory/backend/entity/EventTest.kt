package fr.pentagon.android.mobistory.backend.entity

import android.content.Context
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
import java.time.Instant
import java.util.Date
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
}