package ru.leushinilya.loftmoney.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.screens.Screens

@Preview
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val screens = listOf(Screens.LIST, Screens.LIST, Screens.DIAGRAM)
    Scaffold(
//        topBar = TopBar()
    ) {
        TabRow(selectedTabIndex = 0) {
            screens.forEach {
                Tab(
                    onClick = {
                        navController.navigate(it.name)
                    },
                    selected = false
                )
            }
        }
        NavHost(navController = navController, graph = )
    }
}

@Preview
@Composable
fun TopBar(title: String = Screens.LIST.name) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight(700)
            )
        },
        backgroundColor = colorResource(id = R.color.lightish_blue)
    )
}