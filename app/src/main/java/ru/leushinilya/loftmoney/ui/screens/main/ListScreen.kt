package ru.leushinilya.loftmoney.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.TransactionType
import ru.leushinilya.loftmoney.cells.Item
import ru.leushinilya.loftmoney.ui.themes.LoftTheme

@Composable
fun ListScreen(viewModel: MainViewModel, type: TransactionType) {
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
    SwipeRefresh(
        state = rememberSwipeRefreshState(viewModel.isRefreshing),
        onRefresh = {
            when (type) {
                TransactionType.INCOME -> viewModel.updateIncomes()
                TransactionType.EXPENSE -> viewModel.updateExpenses()
            }
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val list = when (type) {
                TransactionType.EXPENSE -> viewModel.expenses
                TransactionType.INCOME -> viewModel.incomes
            }
            items(items = list, key = { it.id }) { transaction ->
                ItemView(viewModel = viewModel, item = transaction)
                Divider(
                    color = LoftTheme.colors.hint,
                    modifier = Modifier.alpha(0.2F)
                )
            }
        }
    }

}

@Composable
fun ItemView(viewModel: MainViewModel, item: Item) {
    val priceColor = when (item.type) {
        0 -> LoftTheme.colors.expense
        else -> LoftTheme.colors.income
    }
    val backgroundColor = when (item in viewModel.selectedItems) {
        true -> LoftTheme.colors.interactionContentBackground
        else -> LoftTheme.colors.contentBackground
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { viewModel.onItemSelectionChanged(item) },
                    onTap = { viewModel.onItemSelectionChanged(item) }
                )
            }
            .background(backgroundColor)
            .padding(dimensionResource(id = R.dimen.spacing_24)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            color = LoftTheme.colors.primaryText,
            style = LoftTheme.typography.contentNormal
        )
        Text(
            text = item.price,
            style = LoftTheme.typography.contentNormal,
            color = priceColor
        )
    }
}