package miron.gaskov.racelog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import miron.gaskov.racelog.ui.theme.*

@Composable
fun AppBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        SoftBlob(Modifier.offset(x = (-40).dp, y = (-10).dp), size = 200.dp, color = BlobGreen)
        SoftBlob(Modifier.offset(x = 260.dp, y = 80.dp), size = 220.dp, color = BlobBlue)
        SoftBlob(Modifier.offset(x = 40.dp, y = 260.dp), size = 220.dp, color = BlobCyan)
        SoftBlob(Modifier.offset(x = 180.dp, y = 610.dp), size = 240.dp, color = BlobYellow)
        SoftBlob(Modifier.offset(x = 210.dp, y = (-140).dp), size = 240.dp, color = BlobLime)

        content()
    }
}

@Composable
private fun SoftBlob(
    modifier: Modifier,
    size: androidx.compose.ui.unit.Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.20f),
                        color.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                )
            )
    )
}
