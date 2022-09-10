package ru.leushinilya.loftmoney.screens.old.addItem

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.leushinilya.loftmoney.EXPENSE
import ru.leushinilya.loftmoney.INCOME
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.R

class AddItemActivity : ComponentActivity() {

    var compositeDisposable = CompositeDisposable()
    var transactionType: Int? = null

    @Preview
    @Composable
    fun AddItemLayout() {

        var name by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        val color = when (transactionType) {
            INCOME -> colorResource(id = R.color.apple_green)
            EXPENSE -> colorResource(id = R.color.dark_sky_blue)
            else -> colorResource(id = R.color.white_three)
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
                        putItemToInternet(price, name)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionType = intent.getIntExtra("transactionType", -1)
        setContent {
            AddItemLayout()
        }

    }

    private fun putItemToInternet(price: String, name: String) {
        val type = when (transactionType) {
            INCOME -> "income"
            EXPENSE -> "expense"
            else -> return
        }
        val authToken = getSharedPreferences(getString(R.string.app_name), 0)
            .getString(LoftApp.AUTH_KEY, "")
        compositeDisposable.add(
            (application as LoftApp).itemsAPI.postItems(price, name, type, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { finish() },
                    { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show() }
                )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}