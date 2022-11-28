package ru.leushinilya.loftmoney.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.screens.add.AddItemScreen
import ru.leushinilya.loftmoney.screens.login.LoginScreen
import ru.leushinilya.loftmoney.screens.main.MainScreen

@AndroidEntryPoint
@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainActivityViewModel = viewModel()
            val navController: NavHostController = rememberNavController()
            val startDestination = when (viewModel.authorized) {
                true -> Screens.MAIN.name
                false -> Screens.LOGIN.name
            }
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screens.LOGIN.name) { LoginScreen(navController, hiltViewModel()) }
                composable(Screens.MAIN.name) { MainScreen(navController, hiltViewModel()) }
                composable(
                    route = "${Screens.ADD_ITEM.name}/{type}",
                    arguments = listOf(navArgument("type") { type = NavType.StringType })
                ) {
                    AddItemScreen(
                        it.arguments?.getString("type"),
                        onBackPressed = { navController.navigateUp() },
                        hiltViewModel()
                    )
                }
            }
        }
        setTheme(R.style.Theme_Loftmoney)
    }
}