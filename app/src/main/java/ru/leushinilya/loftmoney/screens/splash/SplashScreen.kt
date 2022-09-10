package ru.leushinilya.loftmoney.screens.splash

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.screens.Screens

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = SplashViewModel((LocalContext.current as Activity).application)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.lightish_blue)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_logo),
            contentDescription = "",
            modifier = Modifier.padding(100.dp)
        )
    }
    with(LocalLifecycleOwner.current) {
        lifecycle.addObserver(viewModel)
        viewModel.state.observe(this) { state ->
            when (state) {
                is SplashState.AuthorizedState -> navController.navigate(Screens.MAIN.name)
                is SplashState.NotAuthorizedState -> navController.navigate(Screens.LOGIN.name)
                else -> {}
            }
        }
    }
}