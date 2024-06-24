package com.example.herb.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.herb.screens.HerbDetailScreen
import com.example.herb.screens.HomeScreen
import com.example.herb.ui.theme.HerbTheme

class HerbDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HerbTheme {
                HerbDetailScreen()
            }
        }
    }
}