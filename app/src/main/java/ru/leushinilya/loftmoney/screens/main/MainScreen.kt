package ru.leushinilya.loftmoney.screens.main

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import ru.leushinilya.loftmoney.screens.Screens

@ExperimentalPagerApi
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = MainViewModel(LocalContext.current.applicationContext as Application)
) {
    val screens = listOf(Screens.LIST_EXPENSES, Screens.LIST_INCOMES, Screens.DIAGRAM)
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    LocalLifecycleOwner.current.lifecycle.addObserver(viewModel)
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
            ) { page ->
                when (page) {
                    0 -> ListScreen(viewModel.expenses, viewModel.isRefreshing)
                    1 -> ListScreen(viewModel.incomes, viewModel.isRefreshing)
                    2 -> DiagramScreen(
                        viewModel.expenses.sumOf { it.price.toDouble() }.toFloat(),
                        viewModel.incomes.sumOf { it.price.toDouble() }.toFloat()
                    )
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