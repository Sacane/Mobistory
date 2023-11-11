package fr.pentagon.android.mobistory.backend.entity

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.pentagon.android.mobistory.backend.Database
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class ImageTest {
    private lateinit var db: Database
    private lateinit var imageDao: ImageDao
    private lateinit var eventImageJoin: EventImageJoinDao

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
    }

    @Test
    fun simpleInsertImageTest()  = runTest {
        val id = UUID.randomUUID()
        val image = Image(imageId = id, link = URI_TEMPLATE)
        imageDao.insertImage(image)
        val imageInDb = imageDao.findById(id)
        assertEquals(image, imageInDb)
    }

    fun insertImageWithEventTest()  = runTest {
        val idImage = UUID.randomUUID()
        val image = Image(imageId = idImage, link = URI_TEMPLATE)
        imageDao.insertImage(image)
    }
}