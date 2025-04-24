package com.example.departamentos_udb.modelo

data class Departamento(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val servicios: List<String> = emptyList(),
    val imagenUrl: String = ""
) 