package fr.pentagon.android.mobistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color(
                    red = 255,
                    green = 213,
                    blue = 141,
                    alpha = 255
                )
            )
    ) {
        Text(modifier = Modifier.align(Alignment.Center), text = "MOBISTORY", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.White)
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxSize().background(color = Color(red = 255, green = 213, blue = 141, alpha = 255))) {
        Column(modifier = Modifier.weight(1f).fillMaxSize().background(color = Color.Red)) {
            Text(text = "test")
            Image(painter = painterResource(id = R.drawable.ic_home), contentDescription = "blabla")
        }
        Box(modifier = Modifier.weight(1f).fillMaxSize().background(color = Color.Yellow)) {}
        Box(modifier = Modifier.weight(1f).fillMaxSize().background(color = Color.Red)) {}
        Box(modifier = Modifier.weight(1f).fillMaxSize().background(color = Color.Yellow)) {}
    }
}
