package fr.pentagon.android.mobistory.backend.json

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import fr.pentagon.android.mobistory.test.R
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.runner.RunWith

@Serializable
private data class PersonTestDTO(
    val firstname: String,
    val lastname: String
)

@RunWith(AndroidJUnit4::class)
class JsonFormatterTest {

    @Test
    fun shouldReturnNullWhenLoadAFakeFile() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val content = ctx.loadJSONFile(-1)
        assertNull(content)
    }

    @Test
    fun shouldBeAbleToLoadAFileFromAndroidContext() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val content = ctx.loadJSONFile(R.raw.jsontest)
        assertNotNull(content)
    }

    @Test
    fun shouldBeAbleToLoadEventDTOFromAGoodFileFormatted() {
        val ctx = InstrumentationRegistry.getInstrumentation().context
        val content = ctx.loadJSONFile(R.raw.jsontest)
        val persons = Json.decodeFromString<List<PersonTestDTO>>(content ?: "fail")
        assertEquals(3, persons.size)
        assertTrue(
            persons.any { it.firstname == "johan" && it.lastname == "ramaroson rakotomihamina"}
        )
    }
}

