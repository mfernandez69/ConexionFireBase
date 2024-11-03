package com.example.conexionfirebase

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.conexionfirebase.screens.PantallaInicioSesion
import com.example.conexionfirebase.screens.PantallaPerfil
import com.example.conexionfirebase.screens.PantallaPrincipal
import com.example.conexionfirebase.screens.PantallaRegistroAlumno


@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "pantallaInicioSesion") {
        composable("pantallaInicioSesion") { PantallaInicioSesion(navHostController) }
        composable("pantallaPrincipal") { PantallaPrincipal(navHostController) }
        composable("pantallaRegistroAlumno") { PantallaRegistroAlumno(navHostController) }
        composable("pantallaPerfil") { PantallaPerfil(navHostController) }
    }
}