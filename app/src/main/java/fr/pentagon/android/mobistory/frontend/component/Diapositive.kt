package fr.pentagon.android.mobistory.frontend.component

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import fr.pentagon.android.mobistory.ui.theme.MobistoryTheme

@Composable
fun ImageDisplayer(modifier: Modifier = Modifier, url: String) {
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            modifier = modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .diskCachePolicy(CachePolicy.DISABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .scale(Scale.FILL)
                .build(),
            contentDescription = null,
            filterQuality = FilterQuality.None,
            contentScale = ContentScale.Fit
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

@Composable
fun Diapositive(modifier: Modifier = Modifier, images: List<String>) {
    var index by remember { mutableIntStateOf(0) }
    Row(modifier = modifier.fillMaxSize()) {
        IconButton(onClick = { index = if(index == 0) images.size - 1 else index - 1 }, modifier = Modifier.fillMaxHeight()) {
            Icon(imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = "Previous")
        }
        ImageDisplayer(modifier = Modifier.weight(1f).fillMaxSize(), url = images[index])
        IconButton(onClick = { index = if(index == (images.size - 1)) 0 else index + 1 }, modifier = Modifier.fillMaxHeight()) {
            Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = "Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiapositivePreview() {
    MobistoryTheme {
        Diapositive(images = listOf("https://material.angular.io/assets/img/examples/shiba2.jpg", "https://fastly.picsum.photos/id/142/900/500.jpg?hmac=yW-6OCY-ziKkCVsveX1uRcd1Ew765Mp8Pm0gdn8NRPs"))
    }
}
