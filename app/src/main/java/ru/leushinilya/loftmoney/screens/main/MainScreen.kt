package ru.leushinilya.loftmoney.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.TransactionType
import ru.leushinilya.loftmoney.screens.Screens
import ru.leushinilya.loftmoney.screens.main.diagram.DiagramScreen
import ru.leushinilya.loftmoney.screens.main.list.ListScreen

@Preview
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val screens = listOf(Screens.LIST_EXPENSES, Screens.LIST_INCOMES, Screens.DIAGRAM)
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    Scaffold(
        topBar = { TopBar() }
    ) {
        Column {
            TabRow(selectedTabIndex = selectedTabIndex) {
                screens.forEach {
                    Tab(
                        text = {
                            Text(stringResource(id = it.titleRes))
                        },
                        onClick = {
                            navController.navigate(it.name)
                            selectedTabIndex = screens.indexOf(it)
                        },
                        selected = selectedTabIndex == screens.indexOf(it),
                        modifier = Modifier.background(colorResource(id = R.color.lightish_blue))
                    )
                }
            }
            NavHost(
                navController = navController,
                startDestination = screens[selectedTabIndex].name,
                modifier = Modifier.padding(it)
            ) {
                composable(Screens.LIST_EXPENSES.name) { ListScreen(TransactionType.EXPENSE) }
                composable(Screens.LIST_INCOMES.name) { ListScreen(TransactionType.INCOME) }
                composable(Screens.DIAGRAM.name) { DiagramScreen() }
            }
        }
    }
}

@Preview
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = Screens.MAIN.titleRes),
                color = colorResource(id = R.color.white),
                fontWeight = FontWeight(700)
            )
        },
        backgroundColor = colorResource(id = R.color.lightish_blue)
    )
}