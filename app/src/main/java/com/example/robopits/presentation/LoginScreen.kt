package com.example.robopits.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
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
