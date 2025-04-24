package com.example.departamentos_udb.ui.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.departamentos_udb.modelo.Departamento
import com.example.departamentos_udb.repositorio.DepartamentoRepositorio
import com.example.departamentos_udb.ui.theme.AzulClaro
import com.example.departamentos_udb.ui.theme.AzulSecundario
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearDepartamentoPantalla(
    navegarAtras: () -> Unit,
    onCrearExitoso: () -> Unit,
    repositorio: DepartamentoRepositorio
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var servicios by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    
    var nombreError by remember { mutableStateOf(false) }
    var descripcionError by remember { mutableStateOf(false) }
    var serviciosError by remember { mutableStateOf(false) }
    var imagenError by remember { mutableStateOf(false) }
    
    var mostrarUrlManual by remember { mutableStateOf(false) }
    
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val selectorImagenes = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imagenUri = it
            imagenError = false
        }
    }
    
    fun validarCampos(): Boolean {
        nombreError = nombre.isBlank()
        descripcionError = descripcion.isBlank()
        serviciosError = servicios.isBlank()
        
        imagenError = imagenUri == null && imagenUrl.isBlank()
        
        return !nombreError && !descripcionError && !serviciosError && !imagenError
    }
    
    fun guardarDepartamento() {
        if (validarCampos()) {
            val nuevoId = UUID.randomUUID().toString()
            val listaServicios = servicios.split("\n").filter { it.isNotBlank() }
            
            val nuevoDepartamento = Departamento(
                id = nuevoId,
                nombre = nombre,
                descripcion = descripcion,
                servicios = listaServicios,
                imagenUrl = imagenUrl 
            )
            
            scope.launch {
                try {
                    repositorio.crearDepartamentoConImagen(
                        departamento = nuevoDepartamento,
                        imagenUri = imagenUri,
                        onExito = { onCrearExitoso() }
                    )
                } catch (e: Exception) {
                    
                }
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Departamento") },
                navigationIcon = {
                    IconButton(onClick = navegarAtras) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = nombre,
                onValueChange = { 
                    nombre = it
                    nombreError = false
                },
                label = { Text("Nombre del departamento") },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreError,
                supportingText = {
                    if (nombreError) Text("El nombre es requerido")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = descripcion,
                onValueChange = { 
                    descripcion = it
                    descripcionError = false
                },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                isError = descripcionError,
                supportingText = {
                    if (descripcionError) Text("La descripción es requerida")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                maxLines = 5
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = servicios,
                onValueChange = { 
                    servicios = it
                    serviciosError = false
                },
                label = { Text("Servicios (uno por línea)") },
                modifier = Modifier.fillMaxWidth(),
                isError = serviciosError,
                supportingText = {
                    if (serviciosError) Text("Al menos un servicio es requerido")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                maxLines = 5
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Imagen del departamento",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable { selectorImagenes.launch("image/*") },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (imagenError) MaterialTheme.colorScheme.error else AzulClaro
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (imagenUri != null) {
                        AsyncImage(
                            model = imagenUri,
                            contentDescription = "Vista previa de imagen",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (imagenUrl.isNotBlank()) {
                        AsyncImage(
                            model = imagenUrl,
                            contentDescription = "Vista previa de imagen",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar imagen",
                                tint = AzulSecundario,
                                modifier = Modifier
                                    .height(80.dp)
                                    .aspectRatio(1f)
                            )
                            Text(
                                text = "Toca para seleccionar imagen",
                                color = AzulSecundario
                            )
                        }
                    }
                }
            }
            
            if (imagenError) {
                Text(
                    text = "Se requiere una imagen",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
            TextButton(
                onClick = { mostrarUrlManual = !mostrarUrlManual },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    if (mostrarUrlManual) "Usar imagen del dispositivo" else "Ingresar URL de imagen"
                )
            }
            if (mostrarUrlManual) {
                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { 
                        imagenUrl = it
                        if (it.isNotBlank()) {
                            imagenUri = null
                            imagenError = false
                        }
                    },
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { guardarDepartamento() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Departamento")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
} 