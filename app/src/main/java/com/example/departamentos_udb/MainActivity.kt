package com.example.departamentos_udb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.departamentos_udb.repositorio.DepartamentoRepositorio
import com.example.departamentos_udb.ui.navegacion.NavegacionApp
import com.example.departamentos_udb.ui.theme.DepartamentosudbTheme

class MainActivity : ComponentActivity() {
    // Inicializar el repositorio una sola vez con el contexto de la aplicación
    private val repositorioDepartamento by lazy { DepartamentoRepositorio(applicationContext) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar Firebase ya se realizará automáticamente
        
        enableEdgeToEdge()
        setContent {
            DepartamentosudbTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavegacionApp(repositorio = repositorioDepartamento)
                }
            }
        }
    }
}