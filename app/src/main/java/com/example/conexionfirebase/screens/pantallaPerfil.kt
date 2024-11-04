package com.example.conexionfirebase.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.conexionfirebase.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(navController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .drawBehind {
                        val borderSize = 1.dp.toPx()
                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = borderSize
                        )
                    },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black,
                ),
                title = {
                    Text("Editar perfil")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("pantallaPrincipal")
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        CuerpoPaginaPerfil(innerPadding,navController)
    }
}

@Composable
fun CuerpoPaginaPerfil(innerPadding: PaddingValues,navController: NavHostController) {
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
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Tu perfil",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 40.sp,
                lineHeight = 40.sp
            )
            FormularioPerfil()
        }
        FormEliminarPerfil(navController)
    }
}
@Composable
fun FormEliminarPerfil(navController: NavHostController) {
// Declarar el ViewModel dentro de la función composable
    val viewModel: TuViewModel = viewModel()
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mostrarDialogoPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        if (!mostrarConfirmacion) {
            Button(onClick = { mostrarConfirmacion = true }) {
                Text("Eliminar Perfil")
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        mostrarDialogoPassword = true
                    },
                ) {
                    Text("Confirmar")
                }
                Button(
                    onClick = { mostrarConfirmacion = false }
                ) {
                    Text("Cancelar")
                }
            }
        }
    }

    if (mostrarDialogoPassword) {
        ObtenerContraseñaDelUsuario(
            onPasswordEntered = { password ->
                viewModel.eliminarUsuario(password,navController)
                mostrarDialogoPassword = false
                mostrarConfirmacion = false
            },
            onDismiss = {
                mostrarDialogoPassword = false
            }
        )
    }
}

@Composable
fun ObtenerContraseñaDelUsuario(
    onPasswordEntered: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ingrese su contraseña") },
        text = {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        },
        confirmButton = {
            Button(onClick = { onPasswordEntered(password) }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun FormularioPerfil() {
    val auth: FirebaseAuth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    val coleccion = "alumnos"
    val coroutineScope = rememberCoroutineScope()
    var updateMessage by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                //Buscamos el email mediante el usuaio actuacl en authenticaiton con el campo email
                email = currentUser.email ?: ""

                // Buscar el documento del usuario por email
                val consutla = db.collection(coleccion)
                    .whereEqualTo("email", email)
                    .get()
                    .await()

                if (!consutla.isEmpty) {
                    // Tomar el primer documento que coincida con el email
                    val document = consutla.documents[0]
                    //Obtenemos el valor de los campo del usuario en la DB
                    nombre = document.getString("nombre") ?: ""
                    apellidos = document.getString("apellidos") ?: ""
                } else {
                    error = "No se encontraron datos para este usuario"
                }
            } catch (e: Exception) {
                error = "Error al obtener datos: ${e.message}"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        } else {
            error = "No hay usuario autenticado"
            isLoading = false
        }
    }

    if (isLoading) {
        Text("Cargando...")
    } else if (error != null) {
        Text(error!!)
    } else {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .padding(20.dp)
        ) {
            InputImage()
            OutlinedTextField(
                value = email,
                onValueChange = { },
                label = { Text("EMAIL") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrimario,
                    unfocusedBorderColor = colorPrimario.copy(alpha = 0.5f),
                    focusedTextColor = colorSegundario,
                    unfocusedTextColor = colorPrimario
                ),
                //Solo permitimos la capacidad de leer pero no modificar
                readOnly = true
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("NOMBRE") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrimario,
                    unfocusedBorderColor = colorPrimario.copy(alpha = 0.5f),
                    focusedTextColor = colorSegundario,
                    unfocusedTextColor = colorPrimario
                )
            )

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("APELLIDOS") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrimario,
                    unfocusedBorderColor = colorPrimario.copy(alpha = 0.5f),
                    focusedTextColor = colorSegundario,
                    unfocusedTextColor = colorPrimario
                )
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("NUEVA CONTRASEÑA") },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorPrimario,
                    unfocusedBorderColor = colorPrimario.copy(alpha = 0.5f),
                    focusedTextColor = colorSegundario,
                    unfocusedTextColor = colorPrimario
                )
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            guardarCambios(nombre, apellidos, password)
                            updateMessage = "Cambios guardados con éxito"
                        } catch (e: Exception) {
                            updateMessage = "Error: ${e.message}"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF30C67C),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar Cambios")
            }
            updateMessage?.let {
                Text(it, color = if (it.startsWith("Error")) Color.Red else Color.Green)
            }
        }
    }
}

suspend fun guardarCambios(
    nombre: String,
    apellidos: String,
    newPassword: String
) {
    val user = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    if (user == null) {
        throw Exception("No hay usuario autenticado")
    }

    try {
        // Primero, obtén el documento del usuario
        val userDoc = db.collection("alumnos").whereEqualTo("email", user.email).get().await()
        if (userDoc.documents.isEmpty()) {
            throw Exception("No se encontró el documento del usuario")
        }

        val docId = userDoc.documents[0].id

        // Actualizar datos en Firestore
        db.collection("alumnos").document(docId)
            .update(mapOf(
                "nombre" to nombre,
                "apellidos" to apellidos
            ))
            .await()

        // Si la actualización de Firestore es exitosa, actualizar la contraseña
        if (newPassword.isNotEmpty()) {
            user.updatePassword(newPassword).await()
        }
    } catch (e: Exception) {
        throw Exception("Error al guardar cambios: ${e.message}")
    }
}
@Composable
fun InputImage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, colorPrimario)
            .padding(16.dp)


    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.imgperfil), // Reemplaza con tu recurso de icono
                contentDescription = "Descripción del icono",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Cambiar imagen",
                color = colorSegundario,
                fontSize = 20.sp
            )
        }
    }
}