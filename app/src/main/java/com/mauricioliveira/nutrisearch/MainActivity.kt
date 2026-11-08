package com.mauricioliveira.nutrisearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mauricioliveira.nutrisearch.core.ui.theme.NutriSearchTheme
import com.mauricioliveira.nutrisearch.navigation.NutriSearchNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            NutriSearchTheme {
                NutriSearchNavHost()
            }
        }
    }
}
