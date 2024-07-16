package com.example.robopits.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.robopits.presentation.network.ApiClient
import com.example.robopits.presentation.network.Pedido
import kotlinx.coroutines.launch

@Composable
fun PedidosScreen(userId: String) {
    var pedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        coroutineScope.launch {
            Log.d("PedidosScreen", "User ID: $userId")
            try {
                val response = ApiClient.apiService.getPedidos(userId)
                Log.d("PedidosScreen", "Pedidos Response: $response")
                pedidos = response.pedidos
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                Log.e("PedidosScreen", "Pedidos Error", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (errorMessage.isNotEmpty()) {
            Text(
                errorMessage,
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        } else {
            LazyColumn {
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
            modifier = Modifier
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            Text("Pedido ID: ${pedido._id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Cliente: ${pedido.cliente.nombre}", fontSize = 14.sp)
            Text("Estado: ${pedido.estado}", fontSize = 14.sp)
            Text("Total: \$${pedido.total}", fontSize = 14.sp)
            Text("Productos:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            pedido.productos.forEach { producto ->
                Text("- ${producto.name}: ${producto.quantity} x \$${producto.price}", fontSize = 14.sp)
            }
        }
    }
}
