package com.example.robopits.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.robopits.R
import com.example.robopits.presentation.network.ApiClient
import com.example.robopits.presentation.network.Pedido
import kotlinx.coroutines.launch

@Composable
fun PedidosScreen(userId: String) {
    var pedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var nombreUsuario by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        coroutineScope.launch {
            Log.d("PedidosScreen", "User ID: $userId")
            try {
                val response = ApiClient.apiService.getPedidos(userId)
                Log.d("PedidosScreen", "Pedidos Response: $response")
                if (response.pedidos.isNotEmpty()) {
                    pedidos = response.pedidos
                    nombreUsuario = response.pedidos.first().cliente.nombre
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                Log.e("PedidosScreen", "Pedidos Error", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF001F54)) // Azul marino
    ) {
        if (errorMessage.isNotEmpty()) {
            Text(
                errorMessage,
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Estos son tus",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                        Text(
                            text = "pedidos $nombreUsuario:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(pedidos) { pedido ->
                    PedidoItem(pedido = pedido)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PedidoItem(pedido: Pedido) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Reemplaza R.drawable.logo con tu recurso de imagen
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(40.dp)
                    .padding(bottom = 8.dp)
            )
            Text("Tu pedido:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Estado: ${pedido.estado}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Total: \$${pedido.total}", fontSize = 14.sp)
            Text("Punto de Retiro: ${pedido.puntoDeRetiro ?: "N/A"}", fontSize = 14.sp)
            Text("Productos:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            pedido.productos.forEach { producto ->
                Text("- ${producto.name}: ${producto.quantity} x \$${producto.price}", fontSize = 14.sp)
            }
        }
    }
}
