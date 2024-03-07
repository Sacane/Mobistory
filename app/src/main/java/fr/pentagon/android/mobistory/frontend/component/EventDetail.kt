package fr.pentagon.android.mobistory.frontend.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import fr.pentagon.android.mobistory.backend.Database
import fr.pentagon.android.mobistory.backend.Event
import fr.pentagon.android.mobistory.backend.entity.FavoriteEvent
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme
import fr.pentagon.android.mobistory.ui.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.jsoup.Jsoup
import java.net.URLDecoder

@Composable
fun TitledContent(
    title: String,
    modifier: Modifier = Modifier,
    actionButton: @Composable() (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = Typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            actionButton?.let { it() }
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
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
    var isFavorited by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
    val label = event.label.split("||")
    var openAlertDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(event) {
        withContext(Dispatchers.IO) {
            isFavorited = Database.favoriteDao().isFavorited(event.eventId)
            val language: LanguageUrlReference = if (label.first().isEmpty()) {
                LanguageUrlReference.EN
            } else {
                LanguageUrlReference.FR
            }
            findUrlFromLabel(ctx = context, label = event.title, language = language) { url ->
                findContentPageFromUrl(context, url) {
                    content = it
                }
            }
        }
    }

    TitledContent(event.title, actionButton = {
        IconButton(onClick = {
            if (!isFavorited) {
                openAlertDialog = true
            } else {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        Database.favoriteDao().deleteFavorite(event.eventId)
                    }
                    isFavorited = false
                }
            }
        }) {
            Icon(
                if (isFavorited) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = Color.Magenta,
                modifier = Modifier.size(72.dp)
            )
        }
    }) {
        if (openAlertDialog) {
            FavoriteDialogConfirm(
                onDismissRequest = { openAlertDialog = false },
                onConfirmation = { label ->
                    openAlertDialog = false
                    isFavorited = true
                    scope.launch {
                        Database.favoriteDao().addFavorite(
                            FavoriteEvent(
                                favoriteEventId = event.eventId,
                                customLabel = label
                            )
                        )
                    }
                }
            )
        }
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
        EventDetail(
            context = LocalContext.current,
            Event("||Attempted assassination of Alexandre Millerand")
        )
    }
}

fun findUrlFromLabel(
    ctx: Context,
    language: LanguageUrlReference,
    label: String,
    onRetrieve: (String) -> Unit
) {
    val apiUrl =
        "https://${language.representation}.wikipedia.org/w/api.php?action=opensearch&format=json&search=${
            label.replace(
                " ",
                "%20"
            )
        }"
    val queue = Volley.newRequestQueue(ctx)
    val request = StringRequest(
        Request.Method.GET,
        apiUrl, { response ->
            val url = URLDecoder.decode(
                JSONArray(response)[3].toString().split(",")[0].removeSurrounding(
                    "[\"",
                    "\"]"
                ), "UTF-8"
            ).replace("[\\\\\\[\"]".toRegex(), "")
            Log.i("url", url)
            onRetrieve(url)
        }
    ) { error ->
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
    }
) {
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url, { response ->
            val document = Jsoup.parse(response)
            val mainContentDiv = document.select("div.mw-body-content")
            mainContentDiv.select("figure").remove()
            mainContentDiv.select("sup").remove()
            mainContentDiv.select("div.printfooter").remove()
            if (mainContentDiv.isNotEmpty()) {
                mainContentDiv.first()?.let {
                    onRetrieve(it.text())
                }
            } else {
                Log.w("NO CONTENT", "There is not final content...")
            }
        }
    ) { error ->
        Log.w("error at retrieving -> ", error.message ?: "unknown")
    }
    queue.add(stringRequest)
}