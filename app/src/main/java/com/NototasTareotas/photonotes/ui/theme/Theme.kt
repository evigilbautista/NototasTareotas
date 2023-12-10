package com.NototasTareotas.photonotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.White,
    background = Color.Black,
    onPrimary = Color.Black,
)

val noteVerdeB = Color(0xFF36d19a)
val noteVerdeF = Color(0xFF24c68f)
val tarAzulB = Color(0xFF59a5ff)
val tarAzulF = Color(0xFF2c82e8)

private val LightColorPalette = lightColors(
    primary = Color.White,
    background = Color.White,
   // onPrimary = Color.Black,
)

@Composable
fun PhotoNotesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}