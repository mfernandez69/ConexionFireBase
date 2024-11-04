package com.example.conexionfirebase.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TuViewModel : ViewModel() {

    fun eliminarUsuario(password: String, navController: NavHostController) {
        val user = FirebaseAuth.getInstance().currentUser
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        viewModelScope.launch {
            try {
                // Reautenticar al usuario
                val credential = EmailAuthProvider.getCredential(user?.email ?: "", password)
                user?.reauthenticate(credential)?.await()

                // Buscar el documento del alumno por email
                val resultadoQuery = db.collection("alumnos")
                    .whereEqualTo("email", user?.email)
                    .get()
                    .await()

                if (!resultadoQuery.isEmpty) {
                    // Obtener el ID del documento
                    val documentId = resultadoQuery.documents[0].id

                    // Eliminar el documento de Firestore
                    db.collection("alumnos").document(documentId).delete().await()

                    // Eliminar el usuario de Authentication
                    user?.delete()?.await()

                    // Cerrar sesión
                    auth.signOut()

                    // Navegar a la pantalla de inicio de sesión
                    navController.navigate("pantallaInicioSesion")
                } else {
                    Log.e("EliminarUsuario", "No se encontró el documento del alumno")
                    // Aquí puedes manejar el caso en que no se encuentra el documento
                }
            } catch (e: Exception) {
                Log.e("EliminarUsuario", "Error al eliminar usuario: ${e.message}")
                // Aquí puedes mostrar un mensaje de error al usuario
            }
        }
    }
}