package com.example.departamentos_udb.ui.theme

import androidx.compose.ui.graphics.Color

// Colores principales UDB (Actualizados)
val AzulPrimario = Color(0xFF284292) // Azul principal solicitado
val AzulSecundario = Color(0xFF43A9DF) // Azul secundario solicitado
val AmarilloUDB = Color(0xFFF8DB0D) // Amarillo acento solicitado
val AzulClaro = Color(0xFFD3DFEA) // Azul claro solicitado
val AzulOscuro = Color(0xFF0C0444) // Azul oscuro solicitado

// Colores complementarios (Nuevos)
val RojoAccento = Color(0xFFE74C3C) // Rojo vivo que combina bien con el azul
val VerdeExito = Color(0xFF27AE60) // Verde esmeralda para confirmaciones
val NaranjaAccento = Color(0xFFF39C12) // Naranja cálido para elementos destacados
val TurquesaAccento = Color(0xFF1ABC9C) // Turquesa como color complementario
val VioletaAccento = Color(0xFF8E44AD) // Violeta que complementa el azul principal

// Colores de fondo
val FondoClaro = AzulClaro.copy(alpha = 0.3f) // Azul claro transparente para fondo modo claro
val FondoOscuro = AzulOscuro // Para fondo modo oscuro
val FondoNeutro = Color(0xFFF5F7FA) // Gris muy claro para fondos alternativos

// Colores de texto
val TextoPrimario = AzulOscuro // Para texto principal en modo claro
val TextoSecundario = AzulOscuro.copy(alpha = 0.6f) // Para texto secundario en modo claro
val TextoSobreOscuro = Color.White // Para texto sobre fondos oscuros
val TextoDeshabilitado = Color.Gray // Para texto deshabilitado

// Variantes adicionales
val AzulPrimarioLight = Color(0xFF4565B2) // Versión más clara del azul primario
val AzulSecundarioDark = Color(0xFF3085B0) // Versión más oscura del azul secundario
val AmarilloUDBDark = Color(0xFFDDC40C) // Versión más oscura del amarillo para mejor contraste

// Colores del sistema (para mantener compatibilidad)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)