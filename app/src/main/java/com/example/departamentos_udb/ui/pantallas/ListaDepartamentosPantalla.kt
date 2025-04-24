package com.example.departamentos_udb.ui.pantallas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.departamentos_udb.repositorio.DepartamentoRepositorio
import com.example.departamentos_udb.ui.componentes.TarjetaDepartamento

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDepartamentosPantalla(
    navegarADetalle: (String) -> Unit,
    navegarACrear: () -> Unit,
    repositorio: DepartamentoRepositorio,
    onCerrarSesion: () -> Unit
) {
    val departamentos = repositorio.departamentos.collectAsState().value
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Departamentos UDB", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = onCerrarSesion) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navegarACrear,
                containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Departamento",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        if (departamentos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay departamentos disponibles",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp)
            ) {
                items(departamentos) { departamento ->
                    TarjetaDepartamento(
                        departamento = departamento,
                        onClick = { navegarADetalle(departamento.id) }
                    )
                }
            }
        }
    }
} 