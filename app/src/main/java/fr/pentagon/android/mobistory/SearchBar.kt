package fr.pentagon.android.mobistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

@Composable
fun SearchBarComponent(modifier: Modifier = Modifier, componentHeight: Int = 50) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        TextField(
            modifier = Modifier.weight(1f),
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search...") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                //TODO
            })
        )

        Image(
            painter = painterResource(id = R.drawable.filter),
            contentDescription = "Filter",
            modifier = Modifier.size(width = componentHeight.dp, height = componentHeight.dp).clickable {
                //TODO
            }

        )

        Image(
            painter = painterResource(id = R.drawable.eye),
            contentDescription = "Change view",
            modifier = Modifier.size(width = componentHeight.dp, height = componentHeight.dp).clickable {
                //TODO
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    MobistoryTheme {
        SearchBarComponent()
    }
}

