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
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class ImageTest {
    private lateinit var db: Database
    private lateinit var imageDao: ImageDao
    private lateinit var eventImageJoin: EventImageJoinDao
    private lateinit var eventDao: EventDao

    companion object{
        val URI_TEMPLATE: Uri = Uri.parse("https://localhost:8088/image.png")
    }

    @Before
    fun init() {
        val context: Context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context,
            Database::class.java
        ).build()
        imageDao = db.imageDao()
        eventImageJoin = db.eventImageJoinDao()
        eventDao = db.eventDao()
    }

    @Test
    fun simpleInsertImageTest()  = runTest {
        val id = UUID.randomUUID()
        val image = Image(imageId = id, link = URI_TEMPLATE)
        imageDao.insertImage(image)
        val imageInDb = imageDao.findById(id)
        assertEquals(image, imageInDb)
    }

    @Test
    fun imageCanBeInsertInAssociationWithEvents()  = runTest {
        // Image
        val idImage = UUID.randomUUID()
        val image = Image(imageId = idImage, link = URI_TEMPLATE)

        val idImage2 = UUID.randomUUID()
        val image2 = Image(imageId = idImage2, link = Uri.parse("https://localhost:8088/image2.png"))
        // Event
        val eventId = UUID.randomUUID()
        val event = Event(eventId = eventId, label = "Hello", startDate =  Date.from(Instant.now()), endDate = Date.from(
            Instant.now().minusSeconds(403820)), wikipedia = "I don't know")

        // Creation of Join for last created image and event
        val join = EventImageJoin(eventId, idImage)
        val join2 = EventImageJoin(eventId, idImage2)
        // Save the relation in database
        imageDao.insertImage(image)
        imageDao.insertImage(image2)
        eventDao.save(event)
        eventImageJoin.save(join)
        eventImageJoin.save(join2)


        // check the relation

        val eventWithImage = eventDao.findEventWithImageById(eventId)

        assertNotNull(eventWithImage)
        assertEquals(eventWithImage!!.event, event)
        assertTrue(image in eventWithImage.images)
        assertTrue(image2 in eventWithImage.images)

    }
}