package ru.leushinilya.loftmoney.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import ru.leushinilya.loftmoney.ui.screens.add.AddItemScreen
import ru.leushinilya.loftmoney.ui.screens.login.LoginScreen
import ru.leushinilya.loftmoney.ui.screens.login.LoginViewModel
import ru.leushinilya.loftmoney.ui.screens.main.MainScreen
import ru.leushinilya.loftmoney.ui.screens.main.MainViewModel
import ru.leushinilya.loftmoney.ui.themes.MainTheme
import ru.leushinilya.loftmoney.ui.themes.UiSettings

@AndroidEntryPoint
@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val mainViewModel: MainViewModel = hiltViewModel()
            val loginViewModel: LoginViewModel = hiltViewModel()
            val uiSettings = viewModel.uiSettings.collectAsState(UiSettings())
            val authorized = viewModel.authorized.collectAsState(null)
            val startDestination = when (authorized.value) {
                true -> Screens.MAIN.name
                false -> Screens.LOGIN.name
                null -> return@setContent
            }
            MainTheme(uiSettings.value) {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screens.LOGIN.name) { LoginScreen(loginViewModel) }
                    composable(Screens.MAIN.name) { MainScreen(navController, mainViewModel) }
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
                when (authorized.value) {
                    true -> navController.navigate(Screens.MAIN.name) { launchSingleTop = true }
                    false -> navController.navigate(Screens.LOGIN.name) {
                        launchSingleTop = true
                        popUpTo(startDestination) { inclusive = true }
                    }
                    null -> {}
                }
            }
        }
    }

}