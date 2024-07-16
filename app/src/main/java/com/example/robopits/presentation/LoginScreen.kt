package com.example.robopits.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.robopits.R
import com.example.robopits.presentation.network.ApiClient
import com.example.robopits.presentation.network.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun LoginScreen(onLoginSuccess: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Reemplaza R.drawable.logo con tu recurso de imagen
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(60.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = "Robopits",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF001F54) // Azul marino
        )
        Text(
            text = "Inicia Sesión",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF001F54) // Azul marino
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Ingresa tu Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Ingresa tu Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    Log.d("LoginScreen", "Email: $email, Password: $password")
                    val loginRequest = LoginRequest(email, password)
                    Log.d("LoginScreen", "Login Request: $loginRequest")
                    try {
                        val response = ApiClient.apiService.login(loginRequest)
                        Log.d("LoginScreen", "Login Response: $response")
                        if (response.success) {
                            onLoginSuccess(response.id)
                        } else {
                            errorMessage = "Login failed"
                        }
                    } catch (e: HttpException) {
                        errorMessage = "HTTP error: ${e.response()?.errorBody()?.string()}"
                        Log.e("LoginScreen", "HTTP error", e)
                    } catch (e: Exception) {
                        errorMessage = "Error: ${e.message}"
                        Log.e("LoginScreen", "Login Error", e)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF001F54)), // Azul marino
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (errorMessage.isNotEmpty()) {
            Text(
                errorMessage,
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
