package ru.leushinilya.loftmoney.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.screens.Screens
import ru.leushinilya.loftmoney.screens.main.diagram.DiagramScreen
import ru.leushinilya.loftmoney.screens.main.list.ListScreen

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
                    text = {
                        Text(stringResource(id = it.titleRes))
                    },
                    onClick = {
                        navController.navigate(it.name)
                    },
                    selected = false,
                    modifier = Modifier.background(colorResource(id = R.color.lightish_blue))
                )
            }
        }
        NavHost(
            navController = navController,
            startDestination = screens[0].name,
            modifier = Modifier.padding(it)
        ) {
            composable(Screens.LIST.name) { ListScreen() }
            composable(Screens.LIST.name) { ListScreen() }
            composable(Screens.DIAGRAM.name) { DiagramScreen() }
        }
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