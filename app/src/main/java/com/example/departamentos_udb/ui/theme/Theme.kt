package com.example.departamentos_udb.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ColorSchemaClaro = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = TextoSobreOscuro,
    primaryContainer = AzulPrimarioLight,
    onPrimaryContainer = TextoSobreOscuro,
    
    secondary = AzulSecundario,
    onSecondary = TextoSobreOscuro,
    secondaryContainer = AzulSecundario.copy(alpha = 0.7f),
    onSecondaryContainer = TextoSobreOscuro,
    
    tertiary = AmarilloUDB,
    onTertiary = AzulOscuro, // Color oscuro sobre amarillo para buen contraste
    tertiaryContainer = AmarilloUDBDark,
    onTertiaryContainer = AzulOscuro,
    
    background = FondoNeutro,
    onBackground = TextoPrimario,
    
    surface = FondoClaro,
    onSurface = TextoPrimario,
    surfaceVariant = AzulClaro,
    onSurfaceVariant = TextoSecundario,
    
    error = RojoAccento,
    onError = TextoSobreOscuro,
    errorContainer = RojoAccento.copy(alpha = 0.7f),
    onErrorContainer = TextoSobreOscuro
)

private val ColorSchemaOscuro = darkColorScheme(
    primary = AzulSecundario, // Usamos el azul secundario como primario en modo oscuro para mejor visibilidad
    onPrimary = TextoSobreOscuro,
    primaryContainer = AzulPrimario,
    onPrimaryContainer = TextoSobreOscuro,
    
    secondary = AzulSecundarioDark,
    onSecondary = TextoSobreOscuro,
    secondaryContainer = AzulSecundario.copy(alpha = 0.5f),
    onSecondaryContainer = TextoSobreOscuro,
    
    tertiary = AmarilloUDB,
    onTertiary = AzulOscuro,
    tertiaryContainer = AmarilloUDBDark,
    onTertiaryContainer = AzulOscuro,
    
    background = AzulOscuro,
    onBackground = TextoSobreOscuro,
    
    surface = AzulOscuro.copy(alpha = 0.8f),
    onSurface = TextoSobreOscuro,
    surfaceVariant = AzulOscuro.copy(alpha = 0.6f),
    onSurfaceVariant = TextoSobreOscuro.copy(alpha = 0.8f),
    
    error = RojoAccento,
    onError = TextoSobreOscuro,
    errorContainer = RojoAccento.copy(alpha = 0.8f),
    onErrorContainer = TextoSobreOscuro
)

@Composable
fun DepartamentosudbTheme(
    modoOscuro: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    colorDinamico: Boolean = false, // Desactivado por defecto para mantener la identidad visual UDB
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        colorDinamico && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (modoOscuro) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        modoOscuro -> ColorSchemaOscuro
        else -> ColorSchemaClaro
    }
    val vista = LocalView.current
    if (!vista.isInEditMode) {
        SideEffect {
            val ventana = (vista.context as Activity).window
            ventana.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(ventana, vista).isAppearanceLightStatusBars = !modoOscuro
            
            // Opcional: Color de la barra de navegación para una experiencia más inmersiva
            ventana.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(ventana, vista).isAppearanceLightNavigationBars = !modoOscuro
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}