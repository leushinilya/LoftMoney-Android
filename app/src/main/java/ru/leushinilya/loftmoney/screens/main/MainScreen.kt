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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.TransactionType
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
//    TODO: Поправить перекраску стутус бара
    val systemUiController = rememberSystemUiController()
    val backgroundColor = if (viewModel.selectedCount > 0) {
        colorResource(id = R.color.selection_tab_color)
    } else {
        colorResource(id = R.color.lightish_blue)
    }
    systemUiController.setSystemBarsColor(backgroundColor)
    LocalLifecycleOwner.current.lifecycle.addObserver(viewModel)
    Scaffold(
        topBar = {
            if (viewModel.selectedCount > 0) {
                EditingTopBar(viewModel.selectedCount)
            } else {
                TopBar()
            }
        },
        floatingActionButton = {
            when (pagerState.currentPage) {
                0 -> {
                    AddItemFab { navController.navigate("${Screens.ADD_ITEM.name}/${TransactionType.EXPENSE}") }
                }
                1 -> {
                    AddItemFab { navController.navigate("${Screens.ADD_ITEM.name}/${TransactionType.INCOME}") }
                }
            }
        }
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
                        modifier = Modifier.background(backgroundColor)
                    )
                }
            }
            HorizontalPager(
                count = screens.size,
                state = pagerState,
                modifier = Modifier.padding(it)
            ) { page ->
                when (page) {
                    0 -> ListScreen(viewModel, TransactionType.EXPENSE)
                    1 -> ListScreen(viewModel, TransactionType.INCOME)
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
                fontWeight = FontWeight(700),
                fontSize = 20.sp
            )
        },
        backgroundColor = colorResource(id = R.color.lightish_blue)
    )
}

@Preview
@Composable
fun EditingTopBar(selectedCount: Int = 0) {
    TopAppBar(
        backgroundColor = colorResource(id = R.color.selection_tab_color)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "back_icon",
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "${stringResource(id = R.string.tool_bar_title_selection)} $selectedCount",
            color = colorResource(id = R.color.white),
            fontWeight = FontWeight(700),
            fontSize = 20.sp,
            modifier = Modifier.weight(1F)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_trash),
            contentDescription = "back_icon",
            modifier = Modifier.padding(16.dp)
        )
    }
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