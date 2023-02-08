package ru.leushinilya.loftmoney.ui.screens.main

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.data.remote.entity.Balance
import ru.leushinilya.loftmoney.ui.themes.LoftTheme

@Composable
fun DiagramScreen(viewModel: MainViewModel) {
    val balanceState = viewModel.balance.collectAsState()
    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column {
                Balance(
                    balance = balanceState.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                )
                Diagram(balance = balanceState.value)
            }
        }
        else -> {
            Row {
                Balance(
                    balance = balanceState.value,
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxHeight()
                )
                Diagram(balance = balanceState.value)
            }
        }
    }
}

@Composable
fun Balance(balance: Balance?, modifier: Modifier) {
    val incomesSum = balance?.totalIncomes ?: 0.0
    val expensesSum = balance?.totalExpenses ?: 0.0
    val totalSum = incomesSum - expensesSum
    ConstraintLayout(
        modifier = modifier.background(LoftTheme.colors.contentBackground)
    ) {
        val (available, expenses, incomes) = createRefs()
        Column(modifier = Modifier
            .constrainAs(available) {
                top.linkTo(parent.top)
                bottom.linkTo(expenses.top)
                width = Dimension.matchParent
                height = Dimension.fillToConstraints
            }
            .border(width = 1.dp, color = LoftTheme.colors.hint)
            .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.available_finance),
                style = LoftTheme.typography.contentNormal,
                color = LoftTheme.colors.primaryText
            )
            Text(
                text = totalSum.toString(),
                style = LoftTheme.typography.contentLarge,
                color = LoftTheme.colors.secondaryBackground
            )
        }
        Column(modifier = Modifier
            .constrainAs(expenses) {
                top.linkTo(available.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(incomes.start)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .border(width = 1.dp, color = LoftTheme.colors.hint)
            .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.expences),
                style = LoftTheme.typography.contentSmall,
                color = LoftTheme.colors.primaryText
            )
            Text(
                text = expensesSum.toString(),
                style = LoftTheme.typography.contentNormal,
                color = LoftTheme.colors.expense
            )
        }

        Column(modifier = Modifier
            .constrainAs(incomes) {
                top.linkTo(available.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(expenses.end)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .border(width = 1.dp, color = LoftTheme.colors.hint)
            .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.incomes),
                style = LoftTheme.typography.contentSmall,
                color = LoftTheme.colors.primaryText
            )
            Text(
                text = incomesSum.toString(),
                style = LoftTheme.typography.contentNormal,
                color = LoftTheme.colors.income
            )
        }
    }
}

@Composable
fun Diagram(balance: Balance?) {
    val expensesColor = LoftTheme.colors.expense
    val incomesColor = LoftTheme.colors.income
    val expenses = balance?.totalExpenses?.toFloat() ?: 0F
    val incomes = balance?.totalIncomes?.toFloat() ?: 0F
    val expensesAngle = (expenses / (expenses + incomes) * 360)
    val incomesAngle = (incomes / (expenses + incomes) * 360)

    Canvas(
        modifier = Modifier
            .aspectRatio(1F)
            .padding(16.dp)
    ) {
        drawArc(
            color = expensesColor,
            startAngle = 180 - expensesAngle / 2,
            sweepAngle = expensesAngle,
            useCenter = true,
            topLeft = Offset(0F, 25F),
            size = this.size.copy(height = size.height - 50F, width = size.width - 50F)
        )

        drawArc(
            color = incomesColor,
            startAngle = 360 - incomesAngle / 2,
            sweepAngle = incomesAngle,
            useCenter = true,
            topLeft = Offset(50F, 25F),
            size = this.size.copy(height = size.height - 50F, width = size.width - 50F)
        )
    }
}