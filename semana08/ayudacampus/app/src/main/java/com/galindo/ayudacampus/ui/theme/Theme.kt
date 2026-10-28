package com.galindo.ayudacampus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.galindo.ayudacampus.data.model.Rol

private val DarkColorScheme = darkColorScheme(
    primary = CyanPrimary,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = CyanPrimary,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun AyudacampusTheme(
    rol: Rol? = Rol.CLIENTE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val primaryColor = when (rol) {
        Rol.ADMIN -> VioletPrimary
        Rol.CLIENTE -> CyanPrimary
        else -> CyanPrimary
    }

    val baseScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> baseScheme.copy(primary = primaryColor)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun AyudaTheme(
    rol: Rol? = Rol.CLIENTE,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AyudacampusTheme(
        rol = rol,
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content
    )
}