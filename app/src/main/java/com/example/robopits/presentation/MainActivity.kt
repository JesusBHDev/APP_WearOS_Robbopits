package com.example.robopits.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var userId by remember { mutableStateOf<String?>(null) }

            if (userId == null) {
                LoginScreen(onLoginSuccess = { id -> userId = id })
            } else {
                PedidosScreen(userId = userId!!)
            }
        }
    }
}
