package ru.leushinilya.loftmoney.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.TransactionType
import ru.leushinilya.loftmoney.ui.screens.Screens
import ru.leushinilya.loftmoney.ui.screens.settings.SettingsDialog
import ru.leushinilya.loftmoney.ui.themes.LoftTheme

@ExperimentalPagerApi
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val screens = listOf(Screens.LIST_EXPENSES, Screens.LIST_INCOMES, Screens.DIAGRAM)
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val systemUiController = rememberSystemUiController()
    val backgroundColor = if (viewModel.selectedItems.isNotEmpty()) {
        LoftTheme.colors.interactionBackground
    } else {
        LoftTheme.colors.primaryBackground
    }
    systemUiController.setStatusBarColor(backgroundColor)
    Scaffold(
        topBar = {
            if (viewModel.selectedItems.isNotEmpty()) {
                EditingTopBar(
                    onCancelClicked = { viewModel.onCancelClicked() },
                    onRemoveClicked = { viewModel.onRemoveClicked() },
                    selectedCount = viewModel.selectedItems.size
                )
            } else {
                TopBar { viewModel.onLogoutClicked() }
            }
        },
        floatingActionButton = {
            when (pagerState.currentPage) {
                0 -> {
                    AddItemFab { navController.navigate("${Screens.ADD_ITEM.name}/${TransactionType.EXPENSE.value}") }
                }
                1 -> {
                    AddItemFab { navController.navigate("${Screens.ADD_ITEM.name}/${TransactionType.INCOME.value}") }
                }
            }
        }
    ) {
        Column {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    TabRowDefaults.Indicator(
                        color = LoftTheme.colors.secondaryBackground,
                        modifier = Modifier.pagerTabIndicatorOffset(pagerState, it)
                    )
                }
            ) {
                screens.forEach {
                    Tab(
                        text = {
                            Text(
                                text = stringResource(id = it.titleRes),
                                style = LoftTheme.typography.tabs,
                                color = LoftTheme.colors.contentBackground
                            )
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
                    2 -> DiagramScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun TopBar(onLogoutClicked: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = Screens.MAIN.titleRes),
                color = LoftTheme.colors.contentBackground,
                style = LoftTheme.typography.toolbar
            )
        },
        backgroundColor = LoftTheme.colors.primaryBackground,
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = LoftTheme.colors.contentBackground
                )
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(
                    onClick = {
                        showMenu = false
                        onLogoutClicked()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        style = LoftTheme.typography.contentNormal,
                        color = LoftTheme.colors.primaryText
                    )
                }
                DropdownMenuItem(
                    onClick = {
                        showSettings = true
                        showMenu = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = LoftTheme.typography.contentNormal,
                        color = LoftTheme.colors.primaryText
                    )
                }
            }
        }
    )
    if (showSettings) {
        SettingsDialog { showSettings = false }
    }
}

@Composable
fun EditingTopBar(
    onCancelClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    selectedCount: Int
) {
    TopAppBar(
        backgroundColor = LoftTheme.colors.interactionBackground
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "back_icon",
            modifier = Modifier
                .padding(16.dp)
                .clickable { onCancelClicked() }
        )
        Text(
            text = "${stringResource(id = R.string.tool_bar_title_selection)} $selectedCount",
            color = LoftTheme.colors.contentBackground,
            style = LoftTheme.typography.toolbar,
            modifier = Modifier.weight(1F)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_trash),
            contentDescription = "back_trash",
            modifier = Modifier
                .padding(16.dp)
                .clickable { onRemoveClicked() }
        )
    }
}

@Composable
fun AddItemFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = LoftTheme.colors.secondaryBackground
    ) {
        Image(painter = painterResource(id = R.drawable.add_icon), contentDescription = "add")
    }
}