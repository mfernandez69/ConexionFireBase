package com.example.conexionfirebase.componibles

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun BtnInicioSesion(navController: NavHostController){
    Button(
        onClick = { navController.navigate(route = "PantallaMenu") },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue
        )
    ) {
        Text(
            text = "Entrar",
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
    }
}