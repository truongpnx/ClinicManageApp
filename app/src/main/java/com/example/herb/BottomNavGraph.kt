package com.example.herb

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.herb.event.HerbEvent
import com.example.herb.model.HerbViewModel
import com.example.herb.screens.BottomBarScreen
import com.example.herb.screens.HerbScreen
import com.example.herb.screens.PatientScreen
import com.example.herb.screens.SettingScreen
import com.example.herb.state.HerbState

@Composable
fun BottomNavGraph(navController: NavHostController, paddingValues: PaddingValues) {

    val herbViewModel = hiltViewModel<HerbViewModel>()

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Patient.route
    ) {
        Log.d("BottomNavGraph", "NavHost")

        composable(route = BottomBarScreen.Patient.route) {
            PatientScreen(paddingValues)
        }
        composable(route = BottomBarScreen.Herb.route) {
            val state by herbViewModel.state.collectAsState()
            HerbScreen(state, herbViewModel::onEvent, paddingValues)
        }
        composable(route = BottomBarScreen.Setting.route) {
            SettingScreen(paddingValues)
        }
    }
}