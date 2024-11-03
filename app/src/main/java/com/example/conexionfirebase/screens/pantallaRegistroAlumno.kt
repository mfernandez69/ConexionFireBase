@file:Suppress("SpellCheckingInspection")

package com.example.conexionfirebase.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun PantallaRegistroAlumno(navController: NavHostController) {
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Registrate",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 40.sp,
                lineHeight = 40.sp
            )
            FormularioRegistroAlumno(navController)
        }
    }
}

@Composable
fun FormularioRegistroAlumno(navController: NavHostController) {
    val auth: FirebaseAuth = Firebase.auth
    //val context = LocalContext.current
    //Creamos una variable para mostrar el estado de carga
    var loading by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
    ) {
        //Declaramos las variables de estado que utilizaremos para el formulario
        var email by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var nombre by remember { mutableStateOf("") }
        var apellidos by remember { mutableStateOf("") }
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
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("NOMBRE") },
            shape = RoundedCornerShape(16.dp)
        )
        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("APELLIDOS") },
            shape = RoundedCornerShape(16.dp)
        )
        val db = FirebaseFirestore.getInstance()
        val coleccion = "alumnos"
        Spacer(modifier = Modifier.height(20.dp))
        /*
         Logica del boton de registro:
        -1ºSe verifica que todos los campos esten llenos
        -2ºSe crea un usuario en fireBase authentication
        -3ºSe hace un recuento de alumnos en la base de datos para asignar la nueva id
        -4ºSe comprueba que el alumno con la nueva id no exista en la base de datos antes de asignarla
        -5ºSi no existe nadie con la misma id y los pasos anteriores estan bien, se añade el nuevo alumno
        * */
        Button(
            onClick = {
                loading = true
                if (email.isNotBlank() && contrasena.isNotBlank() && nombre.isNotBlank() && apellidos.isNotBlank()) {
                    auth.createUserWithEmailAndPassword(email, contrasena)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Usuario creado exitosamente en Authentication
                                //Para obtener el usuario actual de firebase usamos auth.currentUser
                                val user = auth.currentUser
                                //Si no hay un usuario la funcion termina prematuramente con el operador elvis
                                user?.uid ?: return@addOnCompleteListener
                                obtenerUltimoIdAlumno { ultimoId ->
                                    if (ultimoId >= 0) {
                                        val nuevoId = (ultimoId + 1).toString()
                                        val dato = hashMapOf(
                                            "id" to nuevoId,
                                            "email" to email,
                                            "nombre" to nombre,
                                            "apellidos" to apellidos
                                        )

                                        db.collection(coleccion).document(nuevoId)
                                            .get()
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val document = task.result
                                                    if (document != null && document.exists()) {
                                                        mensajeDeError = "El ID de alumno ya existe"
                                                        loading = false
                                                    } else {
                                                        db.collection(coleccion)
                                                            .document(nuevoId)
                                                            .set(dato)
                                                            .addOnSuccessListener {
                                                                mensajeDeError = "Datos guardados correctamente"
                                                                email = ""
                                                                contrasena = ""
                                                                nombre = ""
                                                                apellidos = ""
                                                                loading = false
                                                                navController.navigate("pantallaPrincipal")
                                                            }
                                                            .addOnFailureListener {
                                                                mensajeDeError = "No se ha podido guardar"
                                                                loading = false
                                                            }
                                                    }
                                                } else {
                                                    mensajeDeError = "Error al verificar el ID de alumno"
                                                    loading = false
                                                }
                                            }
                                    } else {
                                        mensajeDeError = "Error al contar alumnos"
                                        loading = false
                                    }

                                }

                            } else {
                                mensajeDeError =
                                    "Error al crear usuario: ${task.exception?.message}"
                                loading = false
                            }
                        }


                } else {
                    mensajeDeError = "Por favor, rellene todos los campos"
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
                    text = "Registrarse",
                    fontSize = 20.sp
                )
            }
        }
        Button(
            onClick = { navController.navigate(route = "pantallaInicioSesion") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF30C67C),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Iniciar sesión",
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
fun obtenerUltimoIdAlumno(callback: (Int) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val coleccion = "alumnos"
    //Creamos la conexion con la base de datos
    db.collection(coleccion)
        //Hacemos una sentencia query donde hacemos un select seleccionando el id mas alto
        .orderBy("id", Query.Direction.DESCENDING)
        .limit(1)
        .get()
        .addOnSuccessListener { result ->
            if (!result.isEmpty) {
                val ultimoDocumento = result.documents[0]
                val ultimoId = ultimoDocumento.getString("id")?.toIntOrNull() ?: 0
                callback(ultimoId)
            } else {
                callback(0) // Si no hay documentos, empezamos desde 0
            }
        }
        .addOnFailureListener { exception ->
            Log.w("ObtenerUltimoIdAlumno", "Error obteniendo el último ID", exception)
            callback(-1) // Retorna -1 en caso de error
        }
}
