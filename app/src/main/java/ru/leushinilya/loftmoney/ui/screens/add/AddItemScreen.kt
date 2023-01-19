package ru.leushinilya.loftmoney.ui.screens.add

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.TransactionType

@Preview
@Composable
fun AddItemScreen(
    type: String? = null,
    onBackPressed: () -> Unit = {},
    viewModel: AddItemViewModel = viewModel()
) {

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val color = when (type) {
        TransactionType.INCOME.value -> colorResource(id = R.color.apple_green)
        TransactionType.EXPENSE.value -> colorResource(id = R.color.lightish_blue)
        else -> return
    }

    val viewState = viewModel.state.observeAsState()
    when (val state = viewState.value) {
        is AddItemViewState.Error -> Toast.makeText(
            LocalContext.current,
            state.message,
            Toast.LENGTH_LONG
        ).show()
        is AddItemViewState.Success -> onBackPressed()
        else -> {}
    }

    Box(
        modifier = Modifier
            .background(color = colorResource(id = R.color.white))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.edittext_title_hint)) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.white),
                    textColor = color,
                    focusedIndicatorColor = color,
                    focusedLabelColor = color,
                    cursorColor = color
                ),
                textStyle = TextStyle(fontSize = 24.sp)
            )

            TextField(
                value = price,
                onValueChange = { price = it },
                label = { Text(stringResource(id = R.string.edittext_price_hint)) },
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacing_16)),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.white),
                    textColor = color,
                    focusedIndicatorColor = color,
                    focusedLabelColor = color,
                    cursorColor = color
                ),
                textStyle = TextStyle(fontSize = 24.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = {
                    viewModel.onAddClicked(price.toFloat(), name, type)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.white)
                ),
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacing_16))
            ) {
                val buttonContentColor = if (name.isNotBlank() && price.isNotBlank()) {
                    color
                } else {
                    colorResource(id = R.color.white_three)
                }
                Image(
                    painter = painterResource(id = R.drawable.check_icon),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(buttonContentColor)
                )
                Text(
                    stringResource(id = R.string.button_add_text),
                    color = buttonContentColor,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.spacing_8)),
                    fontSize = 14.sp
                )
            }

        }
    }

}