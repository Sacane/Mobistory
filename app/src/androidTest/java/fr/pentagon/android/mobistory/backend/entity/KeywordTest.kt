package fr.pentagon.android.mobistory.backend.entity

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.EventDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.util.Date
import java.util.UUID
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class KeywordTest {
    private lateinit var db: Database
    private lateinit var keywordDao: KeywordDao
    private lateinit var keywordEventJoinDao: KeywordEventJoinDao
    private lateinit var eventDao: EventDao

    @Before
    fun init(){
        val ctx = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            ctx,
            Database::class.java
        ).build()
        keywordDao = db.keywordDao()
        keywordEventJoinDao = db.keywordEventJoinDao()
        eventDao = db.eventDao()
    }


    @Test
    fun simpleInsertKeywordTest() = runTest {
        val id = UUID.randomUUID()
        val frKw = Keyword(keywordId = id, label = "FR")
        keywordDao.save(frKw)
        val fromDb = keywordDao.findById(id)

        assertNotNull(fromDb)
        assertEquals(frKw, fromDb)
    }
    @Test
    fun listOfKeywordCanBeSavedAndRetrieve() = runTest {
        val idK1 = UUID.randomUUID()
        val idK2 = UUID.randomUUID()
        val idK3 = UUID.randomUUID()

        val k1 = "FR".asKeyword(idK1)
        val k2 = "fr".asKeyword(idK2)
        val k3 = "war".asKeyword(idK3)
        keywordDao.saveAll(listOf(k1, k2, k3))

        val keywords = keywordDao.getAll()

        assertEquals(3, keywords.size)
        assertTrue(k1 in keywords)
        assertTrue(k2 in keywords)
        assertTrue(k3 in keywords)
    }

    @Test
    fun listOfKeywordCanBeSaveAndRetrieveWithCorrespondentEvent() = runTest {
        val idK1 = UUID.randomUUID()
        val idK2 = UUID.randomUUID()
        val idK3 = UUID.randomUUID()

        val k1 = "FR".asKeyword(idK1)
        val k2 = "fr".asKeyword(idK2)
        val k3 = "war".asKeyword(idK3)
        keywordDao.saveAll(listOf(k1, k2, k3))

        val eventId = Random.nextInt()
        val event = Event(eventId = eventId, label = "Hello", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "I don't know")
        val eventId2 = Random.nextInt()
        val event2 = Event(eventId = eventId2, label = "Hello2", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "I don't know2")

        eventDao.save(event)
        eventDao.save(event2)

        val join = KeywordEventJoin(eventId, idK1)
        val join2 = KeywordEventJoin(eventId, idK2)
        val join3 = KeywordEventJoin(eventId, idK3)

        val joinEvent2 = KeywordEventJoin(eventId2, idK1)

        keywordEventJoinDao.insert(join)
        keywordEventJoinDao.insert(join2)
        keywordEventJoinDao.insert(join3)
        keywordEventJoinDao.insert(joinEvent2)

        val firstJoin = eventDao.findEventWithKeywordById(eventId)
        val secondJoin = eventDao.findEventWithKeywordById(eventId2)

        assertNotNull(firstJoin)
        assertNotNull(secondJoin)

        assertEquals(firstJoin!!.event, event)
        assertTrue(k1 in firstJoin.keywords)
        assertTrue(k2 in firstJoin.keywords)
        assertTrue(k3 in firstJoin.keywords)

        assertEquals(secondJoin!!.event, event2)
        assertTrue(k1 in secondJoin.keywords)
    }
}