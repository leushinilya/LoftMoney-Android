package ru.leushinilya.loftmoney.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
import ru.leushinilya.loftmoney.ui.screens.main.MainScreen
import ru.leushinilya.loftmoney.ui.themes.MainTheme
import ru.leushinilya.loftmoney.ui.themes.UiSettings

@AndroidEntryPoint
@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val uiSettings = viewModel.uiSettings.collectAsState(UiSettings())
            val authorized = viewModel.authorized.collectAsState(null)
            MainTheme(uiSettings.value) {
                val navController: NavHostController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.EMPTY.name,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screens.EMPTY.name) { Empty() }
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
                when (authorized.value) {
                    true -> navController.navigate(Screens.MAIN.name)
                    false -> navController.navigate(Screens.LOGIN.name)
                    null -> {}
                }
            }
        }
    }

    @Composable
    fun Empty() {
    }

}