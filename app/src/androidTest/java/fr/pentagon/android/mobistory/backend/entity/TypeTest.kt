package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.pentagon.android.mobistory.backend.Database
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TypeTest {
    private lateinit var db: Database
    private lateinit var eventDao: EventDao
    private lateinit var eventTypeJoinDao: EventTypeJoinDao
    private lateinit var typeDao: TypeDao
    @Before
    fun init() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            ctx,
            Database::class.java
        ).build()
        eventDao = db.eventDao()
        eventTypeJoinDao = db.eventTypeJoinDao()
        typeDao = db.typeDao()
    }

    @Test
    fun simpleInsertTypeTest() = runTest {
        val id = UUID.randomUUID()
        val type = Type(typeId = id, label = "War")
        typeDao.save(type)
        val typePersisted = typeDao.findById(id)
        assertEquals(type, typePersisted)
    }

    @Test
    fun typeCanBeJoinWithEventTest() = runTest {
        val id = UUID.randomUUID()
        val type = Type(typeId = id, label = "War")
        typeDao.save(type)

        val event1 = Event(label = "event1", startDate =  Date.from(Instant.now()), endDate = Date.from(
        Instant.now().minusSeconds(403820)), wikipedia = "randomUri")

        val event2 = Event(label = "event2", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "randomUri2")

        eventDao.save(event1)
        eventDao.save(event2)

        val join1 = EventTypeJoin(event1.eventId, id)
        val join2 = EventTypeJoin(event2.eventId, id)

        eventTypeJoinDao.save(join1)
        eventTypeJoinDao.save(join2)

        val types = eventDao.findEventWithTypeById(event1.eventId)
        val types2 = eventDao.findEventWithTypeById(event2.eventId)

        assertEquals(1, types.types.size)
        assertEquals(1, types2.types.size)

        assertEquals(type, types.types.first())
        assertEquals(type, types2.types.first())
    }
}