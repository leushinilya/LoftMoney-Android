package ru.leushinilya.loftmoney.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.TransactionType
import ru.leushinilya.loftmoney.screens.Screens
import ru.leushinilya.loftmoney.screens.main.diagram.DiagramScreen
import ru.leushinilya.loftmoney.screens.main.list.ListScreen

@ExperimentalPagerApi
@Composable
fun MainScreen(navController: NavController) {
    val screens = listOf(Screens.LIST_EXPENSES, Screens.LIST_INCOMES, Screens.DIAGRAM)
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = { TopBar() },
        floatingActionButton = { AddItemFab { navController.navigate(Screens.ADD_ITEM.name) } }
    ) {
        Column {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    TabRowDefaults.Indicator(
                        color = colorResource(id = R.color.marigold),
                        modifier = Modifier.pagerTabIndicatorOffset(pagerState, it)
                    )
                }
            ) {
                screens.forEach {
                    Tab(
                        text = {
                            Text(stringResource(id = it.titleRes))
                        },
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(screens.indexOf(it))
                            }
                        },
                        selected = pagerState.currentPage == screens.indexOf(it),
                        modifier = Modifier.background(colorResource(id = R.color.lightish_blue))
                    )
                }
            }
            HorizontalPager(
                count = screens.size,
                state = pagerState,
                modifier = Modifier.padding(it)
            ) {
                when (it) {
                    0 -> ListScreen(TransactionType.EXPENSE)
                    1 -> ListScreen(TransactionType.INCOME)
                    2 -> DiagramScreen()
                }
            }
        }
    }
}

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

@Composable
fun AddItemFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = colorResource(id = R.color.marigold)
    ) {
        Image(painter = painterResource(id = R.drawable.add_icon), contentDescription = "add")
    }
}

@ExperimentalPagerApi
@Preview
@Composable
fun MainScreenPreview() = MainScreen(navController = rememberNavController())