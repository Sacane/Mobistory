package fr.pentagon.android.mobistory.frontend.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import org.json.JSONArray
import org.jsoup.Jsoup
import java.net.URLDecoder

@Composable
fun TitledContent(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
                .padding(bottom = 8.dp)
        )
        content()
    }
}

/**
 * @param context le contexte de l'application (main activity en l'occurence)
 * @param title Le titre de la page à afficher (le label d'un event)
 * @param language Le language de la page à afficher, si un event est en anglais
 */
@Composable
fun EventDetail(context: Context, event: Event) {
    var content by rememberSaveable {
        mutableStateOf("Chargement des données...")
    }
    val scrollState = rememberScrollState()
    val label = event.label.split("||")
    LaunchedEffect(Unit) {
        val language: LanguageUrlReference = if(label.first().isEmpty()){
            LanguageUrlReference.EN
        }else {
            LanguageUrlReference.FR
        }
        findUrlFromLabel(ctx = context, label = event.title, language = language){ url ->
            findContentPageFromUrl(context, url) {
                content = it
            }
        }
    }
    TitledContent(event.title) {
        Column(Modifier.verticalScroll(scrollState)) {
            Text(content)
        }
    }
}

enum class LanguageUrlReference(val representation: String) {
    FR("fr"),
    EN("en");
}

@Composable
@Preview
fun EventDetailPreview() {
    MobistoryTheme {
        EventDetail(context = LocalContext.current, Event("||Attempted assassination of Alexandre Millerand"))
    }
}

fun findUrlFromLabel(ctx: Context, language: LanguageUrlReference, label: String, onRetrieve: (String) -> Unit) {
    val apiUrl = "https://${language.representation}.wikipedia.org/w/api.php?action=opensearch&format=json&search=${label.replace(" ", "%20")}"
    val queue = Volley.newRequestQueue(ctx)
    val request = StringRequest(
        Request.Method.GET,
        apiUrl, { response ->
            val url = URLDecoder.decode(JSONArray(response)[3].toString().split(",")[0].removeSurrounding("[\"", "\"]"), "UTF-8").replace("[\\\\\\[\"]".toRegex(), "")
            Log.i("url", url)
            onRetrieve(url)
        }
    ){error ->
        Log.w("NO CONTENT", "There is no url for this label...")
    }
    queue.add(request)
}

fun findContentPageFromUrl(
    context: Context,
    url: String,
    onRetrieve: (String) -> Unit = { content ->
    Log.i(
        "Content",
        content
    )
}) {
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url, {response ->
            val document = Jsoup.parse(response)
            val mainContentDiv = document.select("div.mw-body-content")
            mainContentDiv.select("figure").remove()
            mainContentDiv.select("sup").remove()
            mainContentDiv.select("div.printfooter").remove()
            if(mainContentDiv.isNotEmpty()) {
                mainContentDiv.first()?.let {
                    onRetrieve(it.text())
                }
            } else {
                Log.w("NO CONTENT", "There is not final content...")
            }
        }
    ) {error ->
        Log.w("error at retrieving -> ", error.message ?: "unknown")
    }
    queue.add(stringRequest)
}