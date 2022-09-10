package ru.leushinilya.loftmoney.screens.main.diagram

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.leushinilya.loftmoney.R

@Preview
@Composable
fun DiagramScreen(viewModel: DiagramViewModel = viewModel()) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
            .padding(16.dp)
    ) {
        val (availableTitle, available, expensesTitle, expenses, incomesTitle, incomes) = createRefs()
        Text(
            text = stringResource(id = R.string.available_finance),
            fontSize = 14.sp,
            color = colorResource(id = R.color.medium_grey),
            modifier = Modifier.constrainAs(availableTitle) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )
        Text(
            text = viewModel.available,
            fontSize = 48.sp,
            color = colorResource(id = R.color.pale_orange),
            fontWeight = FontWeight(500),
            modifier = Modifier.constrainAs(available) {
                top.linkTo(availableTitle.bottom)
                start.linkTo(parent.start)
            }
        )

        Text(
            text = stringResource(id = R.string.expences),
            fontSize = 10.sp,
            color = colorResource(id = R.color.medium_grey),
            modifier = Modifier.constrainAs(expensesTitle) {
                top.linkTo(available.bottom)
                start.linkTo(parent.start)
            }
        )
        Text(
            text = viewModel.expenses,
            fontSize = 24.sp,
            color = colorResource(id = R.color.dark_sky_blue),
            fontWeight = FontWeight(500),
            modifier = Modifier.constrainAs(expenses) {
                top.linkTo(expensesTitle.bottom)
                start.linkTo(expensesTitle.start)
            }
        )

        Text(
            text = stringResource(id = R.string.incomes),
            fontSize = 10.sp,
            color = colorResource(id = R.color.medium_grey),
            modifier = Modifier.constrainAs(incomesTitle) {
                top.linkTo(expensesTitle.top)
                end.linkTo(parent.end)
            }
        )
        Text(
            text = viewModel.incomes,
            fontSize = 24.sp,
            color = colorResource(id = R.color.apple_green),
            fontWeight = FontWeight(500),
            modifier = Modifier.constrainAs(incomes) {
                top.linkTo(expenses.top)
                start.linkTo(incomesTitle.start)
            }
        )
    }


}