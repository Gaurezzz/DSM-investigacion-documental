package com.example.departamentos_udb.ui.navegacion

sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object ListaDepartamentos : Rutas("lista_departamentos")
    object VisualizacionEstudiante : Rutas("visualizacion")
    object DetalleVisualizacion : Rutas("detalle_visualizacion/{departamentoId}") {
        fun crearRuta(departamentoId: String) = "detalle_visualizacion/$departamentoId"
    }
    object DetalleDepartamento : Rutas("detalle_departamento/{departamentoId}") {
        fun crearRuta(departamentoId: String) = "detalle_departamento/$departamentoId"
    }
    object CrearDepartamento : Rutas("crear_departamento")
    object EditarDepartamento : Rutas("editar_departamento/{departamentoId}") {
        fun crearRuta(departamentoId: String) = "editar_departamento/$departamentoId"
    }
} 