package ru.leushinilya.loftmoney.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.leushinilya.loftmoney.screens.login.LoginScreen
import ru.leushinilya.loftmoney.screens.main.MainScreen
import ru.leushinilya.loftmoney.screens.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screens.SPLASH.name,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screens.SPLASH.name) { SplashScreen(navController) }
                composable(Screens.LOGIN.name) { LoginScreen() }
                composable(Screens.MAIN.name) { MainScreen() }
            }
        }
    }
}