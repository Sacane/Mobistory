package fr.pentagon.android.mobistory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

@Composable
fun ImageDisplayer(modifier: Modifier = Modifier, url: String) {
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            modifier = modifier.fillMaxSize(),
            model = url,
            contentDescription = "My async image",
            contentScale = ContentScale.FillBounds
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageDisplayerPreview() {
    MobistoryTheme {
        ImageDisplayer(url = "https://material.angular.io/assets/img/examples/shiba2.jpg")
    }
}
