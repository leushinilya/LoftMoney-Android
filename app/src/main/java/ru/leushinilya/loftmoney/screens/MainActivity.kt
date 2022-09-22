package ru.leushinilya.loftmoney.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.screens.add.AddItemScreen
import ru.leushinilya.loftmoney.screens.login.LoginScreen
import ru.leushinilya.loftmoney.screens.main.MainScreen

@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences(getString(R.string.app_name), 0)
        val accessToken = preferences.getString(LoftApp.AUTH_KEY, "")
        setContent {
            val navController: NavHostController = rememberNavController()
            val startDestination = when (accessToken.isNullOrEmpty()) {
                true -> Screens.LOGIN.name
                false -> Screens.MAIN.name
            }
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screens.LOGIN.name) { LoginScreen(navController) }
                composable(Screens.MAIN.name) { MainScreen(navController) }
                composable(
                    route = "${Screens.ADD_ITEM.name}/{type}",
                    arguments = listOf(navArgument("type") { type = NavType.StringType })
                ) {
                    AddItemScreen(it.arguments?.getString("type"), onBackPressed = { navController.navigateUp() })
                }
            }
        }
        setTheme(R.style.Theme_Loftmoney)
    }
}