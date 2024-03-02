package fr.pentagon.android.mobistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun SearchBarComponent(modifier: Modifier = Modifier, componentHeight: Int = 50, onSearch: (TextFieldValue) -> Unit) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        TextField(
            modifier = Modifier
                .weight(1f)
                .height(height = componentHeight.dp),
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search...") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                onSearch.invoke(searchText)
                //TODO
            })
        )


        Box(
            modifier = Modifier
                .size(width = componentHeight.dp, height = componentHeight.dp)
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = "Filter",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(0.8f)
                    .clickable {
                        //TODO
                    }
            )
        }

        Box(
            modifier = Modifier
                .size(width = componentHeight.dp, height = componentHeight.dp)
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = R.drawable.eye),
                contentDescription = "Change view",
                modifier = Modifier
                    .fillMaxSize()
                    .scale(0.8f)
                    .clickable {
                        //TODO
                    }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchBarPreview() {
    MobistoryTheme {
        SearchBarComponent {}
    }
}

