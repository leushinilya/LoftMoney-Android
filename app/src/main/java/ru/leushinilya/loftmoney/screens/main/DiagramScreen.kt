package ru.leushinilya.loftmoney.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.leushinilya.loftmoney.R

@Composable
fun DiagramScreen(viewModel: MainViewModel) {

    val balanceState = viewModel.balance.collectAsState()
    val incomesSum = balanceState.value?.totalIncomes ?: 0.0
    val expensesSum = balanceState.value?.totalExpenses ?: 0.0
    val balance = incomesSum - expensesSum

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        val (available, expenses, incomes, diagram) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(available) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .fillMaxWidth(1F)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.available_finance),
                fontSize = 14.sp,
                color = colorResource(id = R.color.medium_grey)
            )
            Text(
                text = balance.toString(),
                fontSize = 48.sp,
                color = colorResource(id = R.color.pale_orange),
                fontWeight = FontWeight(500)
            )
        }

        Column(
            modifier = Modifier
                .constrainAs(expenses) {
                    top.linkTo(available.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(incomes.start)
                }
                .fillMaxWidth(0.5F)
                .border(width = 1.dp, color = colorResource(id = R.color.selection_item_color))
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.expences),
                fontSize = 10.sp,
                color = colorResource(id = R.color.medium_grey)
            )
            Text(
                text = expensesSum.toString(),
                fontSize = 24.sp,
                color = colorResource(id = R.color.dark_sky_blue),
                fontWeight = FontWeight(500)
            )
        }

        Column(
            modifier = Modifier
                .constrainAs(incomes) {
                    top.linkTo(available.bottom)
                    start.linkTo(expenses.end)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.5F)
                .border(width = 1.dp, color = colorResource(id = R.color.selection_item_color))
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.incomes),
                fontSize = 10.sp,
                color = colorResource(id = R.color.medium_grey)
            )
            Text(
                text = incomesSum.toString(),
                fontSize = 24.sp,
                color = colorResource(id = R.color.apple_green),
                fontWeight = FontWeight(500)
            )
        }

        Diagram(
            modifier = Modifier
                .constrainAs(diagram) {
                    top.linkTo(expenses.bottom)
                    bottom.linkTo(parent.bottom)
                }
                .padding(16.dp),
            expenses = expensesSum.toFloat(),
            incomes = incomesSum.toFloat()
        )
    }

}

@Composable
fun Diagram(
    modifier: Modifier = Modifier,
    expenses: Float,
    incomes: Float
) {
    val expensesColor = colorResource(id = R.color.dark_sky_blue)
    val incomesColor = colorResource(id = R.color.apple_green)
    val expensesAngle = (expenses / (expenses + incomes) * 360)
    val incomesAngle = (incomes / (expenses + incomes) * 360)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1F)
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