package com.example.departamentos_udb.repositorio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.core.content.FileProvider
import com.example.departamentos_udb.modelo.Departamento
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class DepartamentoRepositorio(private val context: Context) {
    private val TAG = "DepartamentoRepositorio"
    
    // Instancia de Firestore
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("departamentos")
    
    // Directorio para imágenes locales
    private val imagenesDirPath by lazy {
        File(context.filesDir, "imagenes_departamentos").apply {
            if (!exists()) mkdirs()
        }
    }
    
    // Estado para los departamentos
    private val _departamentos = MutableStateFlow<List<Departamento>>(emptyList())
    val departamentos: StateFlow<List<Departamento>> = _departamentos.asStateFlow()
    
    // Instancia de Storage
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference.child("imagenes_departamentos")

    init {
        obtenerDepartamentos()
    }

    private fun obtenerDepartamentos() {
        coleccion.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Error al escuchar cambios", error)
                return@addSnapshotListener
            }
            
            if (snapshot != null) {
                val listaDepartamentos = snapshot.documents.map { documento ->
                    val id = documento.id
                    val nombre = documento.getString("nombre") ?: ""
                    val descripcion = documento.getString("descripcion") ?: ""
                    
                    // Manejar de forma segura la lista de servicios
                    val serviciosAny = documento.get("servicios")
                    val servicios = if (serviciosAny is List<*>) {
                        serviciosAny.filterIsInstance<String>()
                    } else {
                        emptyList()
                    }
                    
                    val imagenUrl = documento.getString("imagenUrl") ?: ""
                    
                    Departamento(
                        id = id,
                        nombre = nombre,
                        descripcion = descripcion,
                        servicios = servicios,
                        imagenUrl = imagenUrl
                    )
                }
                
                _departamentos.value = listaDepartamentos
            }
        }
    }

    private fun convertirDocumentoDepartamento(doc: DocumentSnapshot): Departamento? {
        val id = doc.id
        val nombre = doc.getString("nombre") ?: ""
        val descripcion = doc.getString("descripcion") ?: ""
        val imagenUrl = doc.getString("imagenUrl") ?: ""
        
        // Manejar de forma segura la lista de servicios
        val serviciosAny = doc.get("servicios")
        val servicios = if (serviciosAny is List<*>) {
            serviciosAny.filterIsInstance<String>()
        } else {
            emptyList()
        }
        
        return Departamento(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            servicios = servicios,
            imagenUrl = imagenUrl
        )
    }
    
    // Guardar imagen localmente
    suspend fun guardarImagenLocal(uri: Uri): String = withContext(Dispatchers.IO) {
        try {
            val nombreArchivo = "${UUID.randomUUID()}.jpg"
            val archivoDestino = File(imagenesDirPath, nombreArchivo)
            
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                guardarStream(inputStream, archivoDestino)
            }
            
            // Crear URI de contenido para acceso seguro
            FileProvider.getUriForFile(
                context, 
                "${context.packageName}.fileprovider", 
                archivoDestino
            ).toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar imagen localmente", e)
            throw e
        }
    }
    
    // Copiar el stream de entrada al archivo de destino
    private fun guardarStream(input: InputStream, outputFile: File) {
        FileOutputStream(outputFile).use { outputStream ->
            val buffer = ByteArray(4 * 1024) // 4k buffer
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()
        }
    }
    
    // Crear un nuevo departamento con imagen local
    suspend fun crearDepartamentoConImagen(
        departamento: Departamento,
        imagenUri: Uri? = null,
        onExito: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        try {
            // Si hay uri de imagen, guardar primero localmente
            val imagenUrlFinal = if (imagenUri != null) {
                guardarImagenLocal(imagenUri)
            } else {
                departamento.imagenUrl // Usar la URL que ya viene en el objeto
            }
            
            // Crear el departamento con la URL de la imagen local
            val departamentoConImagen = departamento.copy(imagenUrl = imagenUrlFinal)
            
            val datos = hashMapOf(
                "nombre" to departamentoConImagen.nombre,
                "descripcion" to departamentoConImagen.descripcion,
                "servicios" to departamentoConImagen.servicios,
                "imagenUrl" to departamentoConImagen.imagenUrl
            )
            
            coleccion.add(datos).await()
            onExito()
        } catch (e: Exception) {
            Log.e(TAG, "Error al crear departamento con imagen", e)
            onError(e)
        }
    }
    
    // Actualizar un departamento con nueva imagen o mantener la anterior
    suspend fun actualizarDepartamentoConImagen(
        departamento: Departamento,
        imagenUri: Uri? = null,
        onExito: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        try {
            // Si hay uri de imagen nueva, guardar primero localmente
            val imagenUrlFinal = if (imagenUri != null) {
                guardarImagenLocal(imagenUri)
            } else {
                departamento.imagenUrl // Mantener la URL actual
            }
            
            // Actualizar el departamento con la URL de la imagen
            val departamentoConImagen = departamento.copy(imagenUrl = imagenUrlFinal)
            
            val datos = hashMapOf(
                "nombre" to departamentoConImagen.nombre,
                "descripcion" to departamentoConImagen.descripcion,
                "servicios" to departamentoConImagen.servicios,
                "imagenUrl" to departamentoConImagen.imagenUrl
            )
            
            coleccion.document(departamento.id).set(datos).await()
            onExito()
        } catch (e: Exception) {
            Log.e(TAG, "Error al actualizar departamento con imagen", e)
            onError(e)
        }
    }
    
    // Crear un nuevo departamento
    fun crearDepartamento(departamento: Departamento, onExito: () -> Unit = {}, onError: (Exception) -> Unit = {}) {
        val datos = hashMapOf(
            "nombre" to departamento.nombre,
            "descripcion" to departamento.descripcion,
            "servicios" to departamento.servicios,
            "imagenUrl" to departamento.imagenUrl
        )
        
        coleccion.add(datos)
            .addOnSuccessListener { onExito() }
            .addOnFailureListener { e -> 
                Log.e(TAG, "Error al crear departamento", e)
                onError(e)
            }
    }
    
    // Obtener un departamento por ID
    fun obtenerDepartamento(id: String): Departamento? {
        return _departamentos.value.find { it.id == id }
    }
    
    // Actualizar un departamento existente
    fun actualizarDepartamento(departamento: Departamento, onExito: () -> Unit = {}, onError: (Exception) -> Unit = {}) {
        val datos = hashMapOf(
            "nombre" to departamento.nombre,
            "descripcion" to departamento.descripcion,
            "servicios" to departamento.servicios,
            "imagenUrl" to departamento.imagenUrl
        )
        
        coleccion.document(departamento.id).set(datos)
            .addOnSuccessListener { onExito() }
            .addOnFailureListener { e -> 
                Log.e(TAG, "Error al actualizar departamento", e)
                onError(e)
            }
    }
    
    // Eliminar un departamento
    fun eliminarDepartamento(id: String, onExito: () -> Unit = {}, onError: (Exception) -> Unit = {}) {
        // Primero obtener el departamento para verificar si hay imagen para eliminar
        val departamento = obtenerDepartamento(id)
        
        coleccion.document(id).delete()
            .addOnSuccessListener { 
                // Si el departamento tiene una imagen local, intentar eliminarla
                departamento?.let {
                    try {
                        val uriString = it.imagenUrl
                        if (uriString.startsWith("content://") && uriString.contains(context.packageName)) {
                            // Es una imagen local
                            val uri = Uri.parse(uriString)
                            val path = uri.path?.substringAfterLast("/")
                            path?.let { nombre ->
                                val file = File(imagenesDirPath, nombre)
                                if (file.exists()) {
                                    file.delete()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error al eliminar imagen local", e)
                        // No interrumpir el proceso si falla la eliminación de la imagen
                    }
                }
                onExito() 
            }
            .addOnFailureListener { e -> 
                Log.e(TAG, "Error al eliminar departamento", e)
                onError(e)
            }
    }
} 