package online.bacovsky.trainingmanagement.util

import androidx.compose.ui.graphics.Color

fun Color.darkenBy(percent: Float): Color {
    val red = (red * 255).coerceIn(0f, 255f)
    val green = (green * 255).coerceIn(0f, 255f)
    val blue = (blue * 255).coerceIn(0f, 255f)

    val hsv = FloatArray(3)

    android.graphics.Color.RGBToHSV(red.toInt(), green.toInt(), blue.toInt(), hsv)

    val newValue = (hsv[2] * (1 - percent / 100)).coerceIn(0f, 1f)
    return Color(android.graphics.Color.HSVToColor(floatArrayOf(hsv[0], hsv[1], newValue))).copy(alpha = alpha)
}

fun Color.addTransparencyBy(percent: Float): Color {
    val alphaValue = (alpha * 255).coerceIn(0f, 255f)
    val newAlpha = (alphaValue * (1 - percent / 100)).coerceIn(0f, 255f)

    return copy(alpha = newAlpha / 255f)
}