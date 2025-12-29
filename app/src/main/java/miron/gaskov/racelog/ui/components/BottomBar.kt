package miron.gaskov.racelog.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import miron.gaskov.racelog.R
import miron.gaskov.racelog.navigation.Route

private data class BottomItem(
    val route: Route,
    val title: String,
    val iconRes: Int
)

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomItem(Route.Profile, "Профиль", R.drawable.ic_logo_profile_foreground),
        BottomItem(Route.Groups, "Группы", R.drawable.ic_logo_group_foreground),
        BottomItem(Route.Results, "Результаты", R.drawable.ic_logo_results_foreground),
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val barBg = Color(0xFFEEE9FF)
    val purple = Color(0xFF5F33E1)
    val purple40 = Color(0x665F33E1)

    val density = LocalDensity.current
    val bottomInsetDp = with(density) {
        WindowInsets.navigationBars.getBottom(this).toDp()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp + bottomInsetDp)
            .background(barBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route.path

                val iconTint = if (selected) purple else purple40
                val textColor = purple

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            navController.navigate(item.route.path) {
                                popUpTo(Route.Profile.path) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(item.iconRes),
                        contentDescription = item.title,
                        modifier = Modifier.size(45.dp),
                        colorFilter = ColorFilter.tint(iconTint)
                    )

                    Text(
                        text = item.title,
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(36.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (selected) purple else Color.Transparent)
                    )
                }
            }
        }
    }
}
