package fr.pentagon.android.mobistory

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

@Composable
fun SearchBarComponent(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        Button(onClick = {
            //action
        }) {
            Text("S")
        }

        TextField(
            modifier = Modifier.weight(1f),
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Rechercher...") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                //action
            })
        )

        Button(onClick = {
            //action
        }) {
            Text("F")
        }

        Button(onClick = {
            //action
        }) {
            Text("V")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    MobistoryTheme {
        SearchBarComponent()
    }
}

