package com.example.myapplication5
import android.webkit.WebChromeClient
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.lazy.LazyColumn
import android.view.ViewGroup
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.os.Bundle
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // <--- IMPORTANTE
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication5.ui.theme.MyApplication5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication5Theme {
                // Estado de navegación: loginMyApplication5Theme, registro, galla
                var pantalla by remember { mutableStateOf("login") }

                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    // Navegación principal
                    when (pantalla) {
                        "login" -> LoginScreen(
                            modifier = Modifier.padding(padding),
                            onLoginSuccess = { pantalla = "registro" }
                        )

                        "registro" -> RegistroScreen(
                            modifier = Modifier.padding(padding),
                            onLogout = { pantalla = "login" },
                            onIrAGalla = { pantalla = "galla" }
                        )

                        "galla" -> GallaScreen(
                            modifier = Modifier.padding(padding),
                            onBack = { pantalla = "registro" }
                        )
                    }
                }
            }
        }
    }
}


// --- 1. PANTALLA DE LOGIN ---
@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5)) // Fondo gris claro
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Bienvenido",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF1976D2), // Azul
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (usuario.trim() == "admin" && password.trim() == "1234") {
                    onLoginSuccess()
                } else {
                    mensaje = "❌ Credenciales incorrectas"
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Iniciar Sesión", fontSize = 18.sp)
        }

        if (mensaje.isNotEmpty()) {
            Text(
                mensaje,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


// --- 2. PANTALLA DE REGISTRO ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onIrAGalla: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("Masculino") }
    var pais by remember { mutableStateOf("Colombia") }
    var resultado by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                "Formulario de Registro",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF388E3C)
            )
            Spacer(Modifier.height(20.dp))
        }

        item {
            OutlinedTextField(
                nombre,
                { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            Text("Sexo:", modifier = Modifier.fillMaxWidth())
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(sexo == "Masculino", onClick = { sexo = "Masculino" })
                Text("M")
                Spacer(Modifier.width(20.dp))
                RadioButton(sexo == "Femenino", onClick = { sexo = "Femenino" })
                Text("F")
            }
            Spacer(Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = { onIrAGalla() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)) // Naranja
            ) {
                Text("Ir a Pantalla principal")
            }
            Spacer(Modifier.height(12.dp))
        }

        item {
            Button(
                onClick = { resultado = "Registrado: $nombre ($sexo)" },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
            ) {
                Text("Registrar")
            }
        }

        if (resultado.isNotEmpty()) {
            item {
                Text(
                    resultado,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            TextButton(onClick = onLogout) {
                Text("Cerrar Sesión", color = Color.Red)
            }
        }
    }
}


// --- 3. NUEVA PANTALLA: GALLA ---
@Composable
fun GallaScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    var showPopup by remember { mutableStateOf(false) }

    // Especificamos que es una lista de Color para evitar el error de inferencia
    val imagenesBanner = listOf(
        R.drawable.impala, // Suponiendo que tu archivo se llama imagen1.jpg
        R.drawable.impala67,
    )

    // El pagerState necesita el número de páginas
    val pagerState = rememberPagerState(pageCount = { imagenesBanner.size })

    Column(modifier = modifier.fillMaxSize()) {

        // --- BANNER CON IMÁGENES DESLIZABLES ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        ) { page ->
            // Usamos Box para poder encimar el texto a la imagen
            Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                    painter = painterResource(id = imagenesBanner[page]),
                    contentDescription = "Imagen de perfume",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Texto opcional encima de la imagen
                Text(
                    text = "Imagen ${page + 1}\n(Desliza para pasar)",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f)) // Fondo para que se lea bien
                )
            }
        }

        // CONTENIDO PRINCIPAL
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Variedades de autos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            // --- REPRODUCTOR DE YOUTUBE (WEBVIEW) ---
            val videoUrl = "https://youtu.be/beR8QiIt3c8?si=Hcsg5tdGxJWw6Z4H"
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        // Configuraciones críticas para video
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.mediaPlaybackRequiresUserGesture = false

                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient() // ESTO ES CLAVE PARA VIDEO

                        loadUrl("https://youtu.be/beR8QiIt3c8?si=Hcsg5tdGxJWw6Z4H")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Un poco más alto para que se vea bien el reproductor
                    .background(Color.Black)
            )
            Spacer(Modifier.height(12.dp))

            // --- TEXTO DEBAJO DEL VIDEO ---
            Text(
                text = "En este video veras rugir los motores de diferentes generaciones " +
                        "desde lo clasico que son considerados simbolos hasta bestias modernas .",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { showPopup = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
            ) {
                Text("Ver Detalles Técnicos")
            }
        }

        // FOOTER
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE1BEE7)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("© 2026 - Fragaby & IUE", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        }
    }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            confirmButton = {
                Button(onClick = { showPopup = false }) { Text("Cerrar") }
            },
            title = { Text("Información de Auto") },
            text = { Text("Los autos clasicos vs los modernos siempre han tenido debate.") }
        )
    }
}





