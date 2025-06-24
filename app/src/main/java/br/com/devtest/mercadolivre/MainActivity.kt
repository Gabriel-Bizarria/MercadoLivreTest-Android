package br.com.devtest.mercadolivre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import br.com.devtest.mercadolivre.ui.navigation.NavigationHost
import br.com.devtest.mercadolivre.ui.theme.MercadoLivreDevTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercadoLivreDevTestTheme() {
                Surface (
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationHost()
                }
            }
        }
    }
}