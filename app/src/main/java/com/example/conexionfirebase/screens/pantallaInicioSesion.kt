package com.example.conexionfirebase.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.conexionfirebase.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun PantallaInicioSesion(navController: NavHostController) {

    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF30C67C),  // Color superior
                        Color(0xFF82F4B1)   // Color inferior
                    )
                )
            )
            .fillMaxSize()
            .padding(16.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Text(
                text = "Inicia sesión",
                textAlign = TextAlign.Center,
                fontSize = 40.sp,
                color = Color.White,
                lineHeight = 40.sp
            )
            FormularioInicioSesion(navController)
        }

        ImagenInicioSesion()
    }
}

@Composable
fun FormularioInicioSesion(navController: NavHostController) {
    val auth: FirebaseAuth = Firebase.auth
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(400.dp)
            .padding(20.dp)
    ) {

        var email by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var mensajeDeError by remember { mutableStateOf("") }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("EMAIL") },
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("CONTRASEÑA") },
            //Para no mostrar la contraseña se añade este parámetro
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                loading = true
                if (email.isNotBlank() && contrasena.isNotBlank()) {
                    auth.signInWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener { task ->
                            loading = false
                            if (task.isSuccessful) {
                                // Inicio de sesión exitoso
                                navController.navigate("pantallaPrincipal")
                            } else {
                                // Error en el inicio de sesión
                                mensajeDeError = "Email o contraseña incorrectos"
                            }
                        }
                }else{
                    mensajeDeError = "Por favor,rellena todos los campos"
                    loading = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            enabled = !loading
        ) {

            if (loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(
                    text = "Iniciar sesión",
                    fontSize = 20.sp
                )
            }
        }
        Button(
            onClick = { navController.navigate(route = "pantallaRegistroAlumno") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF30C67C),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Registrarse",
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }

        if (mensajeDeError.isNotEmpty()) {
            Text(
                text = mensajeDeError,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ImagenInicioSesion() {
    val image = painterResource(R.drawable.pixelcut_export__2_)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alpha = 1F
        )
    }
}

