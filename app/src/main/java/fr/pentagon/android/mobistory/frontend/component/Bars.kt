package fr.pentagon.android.mobistory.frontend.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.pentagon.android.mobistory.R

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(color = Color(red = 255, green = 213, blue = 141, alpha = 255))) {
        Text(modifier = Modifier.align(Alignment.Center), text = "MOBISTORY", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.Black)
    }
}

@Composable
fun BottomBar(modifier: Modifier = Modifier, navController: NavController) {
    Row(modifier = modifier.fillMaxSize().background(color = Color(red = 255, green = 213, blue = 141, alpha = 255))) {
        IconBottomBar(modifier = Modifier.weight(1f), icon = R.drawable.ic_home, onClick = { navController.navigate("home") })
        Divider(color = Color.White, modifier = Modifier.fillMaxHeight().width(1.dp))
        IconBottomBar(modifier = Modifier.weight(1f), icon = R.drawable.ic_search, onClick = { navController.navigate("search") })
        Divider(color = Color.White, modifier = Modifier.fillMaxHeight().width(1.dp))
        IconBottomBar(modifier = Modifier.weight(1f), icon = R.drawable.ic_star, onClick = { navController.navigate("favorites") })
        Divider(color = Color.White, modifier = Modifier.fillMaxHeight().width(1.dp))
        IconBottomBar(modifier = Modifier.weight(1f), icon = R.drawable.ic_quiz, onClick = { navController.navigate("quiz") })
    }
}

@Composable
fun IconBottomBar(modifier: Modifier = Modifier, icon: Int, onClick: () -> Unit) {
    BoxWithConstraints(modifier = modifier.fillMaxSize().clickable(onClick = { onClick() }), contentAlignment = Alignment.Center) {
        val constraints = this.constraints

        Box(modifier = Modifier.size((constraints.maxWidth / 2).pxToDp(), (constraints.maxHeight / 2).pxToDp())) {
            Image(painter = painterResource(id = icon), contentDescription = "home", modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }
