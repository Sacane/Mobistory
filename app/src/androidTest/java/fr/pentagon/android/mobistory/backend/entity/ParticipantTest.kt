package fr.pentagon.android.mobistory.backend.entity

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.EventDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class ParticipantTest {

    private lateinit var db: Database
    private lateinit var participantDao: ParticipantDao
    private lateinit var eventDao: EventDao
    private lateinit var eventParticipantJoinDao: EventParticipantJoinDao

    @Before
    fun init() {
        val context: Context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context,
            Database::class.java
        ).build()
        eventDao = db.eventDao()
        participantDao = db.participantDao()
        eventParticipantJoinDao = db.eventParticipantJoinDao()
    }

    @Test
    fun participantDaoTest() = runTest {
        val id = UUID.randomUUID()
        val toInsert = Event(eventId = id, label = "Hello", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "I don't know")
        eventDao.save(toInsert)

        val participantId = UUID.randomUUID()
        val participant = Participant(participantId = participantId, name = "Jean", firstName = "Paul")
        participantDao.save(participant)

        eventParticipantJoinDao.save(EventParticipantJoin(id, participantId))

        val participants = eventDao.findParticipantsByEventId(id)
        assertEquals(1, participants.size)

        assertEquals(participant, participants.first())
    }
}