package com.example.departamentos_udb.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.departamentos_udb.repositorio.DepartamentoRepositorio
import com.example.departamentos_udb.ui.pantallas.CrearDepartamentoPantalla
import com.example.departamentos_udb.ui.pantallas.DetalleAdminPantalla
import com.example.departamentos_udb.ui.pantallas.DetalleDepartamentoPantalla
import com.example.departamentos_udb.ui.pantallas.EditarDepartamentoPantalla
import com.example.departamentos_udb.ui.pantallas.ListaDepartamentosPantalla
import com.example.departamentos_udb.ui.pantallas.LoginPantalla
import com.example.departamentos_udb.ui.pantallas.VisualizacionPantalla

@Composable
fun NavegacionApp(
    navController: NavHostController = rememberNavController(),
    repositorio: DepartamentoRepositorio
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Login.ruta
    ) {
        composable(Rutas.Login.ruta) {
            LoginPantalla(
                onNavigateToAdmin = {
                    navController.navigate(Rutas.ListaDepartamentos.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                },
                onNavigateToEstudiante = {
                    navController.navigate(Rutas.VisualizacionEstudiante.ruta) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Rutas.VisualizacionEstudiante.ruta) {
            VisualizacionPantalla(
                repositorio = repositorio,
                onVerDetalle = { departamentoId ->
                    navController.navigate(Rutas.DetalleVisualizacion.crearRuta(departamentoId))
                },
                onCerrarSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = Rutas.DetalleVisualizacion.ruta,
            arguments = listOf(
                navArgument("departamentoId") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val departamentoId = navBackStackEntry.arguments?.getString("departamentoId") ?: ""
            DetalleDepartamentoPantalla(
                departamentoId = departamentoId,
                repositorio = repositorio,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Rutas.ListaDepartamentos.ruta) {
            ListaDepartamentosPantalla(
                navegarADetalle = { departamentoId ->
                    navController.navigate(Rutas.DetalleDepartamento.crearRuta(departamentoId))
                },
                navegarACrear = {
                    navController.navigate(Rutas.CrearDepartamento.ruta)
                },
                repositorio = repositorio,
                onCerrarSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Rutas.DetalleDepartamento.ruta,
            arguments = listOf(
                navArgument("departamentoId") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val departamentoId = navBackStackEntry.arguments?.getString("departamentoId") ?: ""
            DetalleAdminPantalla(
                departamentoId = departamentoId,
                repositorio = repositorio,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Rutas.EditarDepartamento.crearRuta(id))
                }
            )
        }
        
        composable(Rutas.CrearDepartamento.ruta) {
            CrearDepartamentoPantalla(
                navegarAtras = { navController.popBackStack() },
                onCrearExitoso = { navController.popBackStack() },
                repositorio = repositorio
            )
        }
        
        composable(
            route = Rutas.EditarDepartamento.ruta,
            arguments = listOf(
                navArgument("departamentoId") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val departamentoId = navBackStackEntry.arguments?.getString("departamentoId") ?: ""
            EditarDepartamentoPantalla(
                departamentoId = departamentoId,
                navegarAtras = { navController.popBackStack() },
                onEditarExitoso = { navController.popBackStack() },
                repositorio = repositorio
            )
        }
    }
} 