package ru.leushinilya.loftmoney.screens.addItem

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.leushinilya.loftmoney.EXPENSE
import ru.leushinilya.loftmoney.INCOME
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.R

class AddItemActivity : AppCompatActivity() {
    var addButton: Button? = null
    var nameEditText: EditText? = null
    var priceEditText: EditText? = null
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

        Box(modifier = Modifier
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
                        textColor = color
                    )
                )

                TextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(stringResource(id = R.string.edittext_price_hint)) },
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacing_16)),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colorResource(id = R.color.white),
                        textColor = color
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(
                    onClick = { },
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
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.spacing_8))
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
//        setContentView(R.layout.activity_add_item)
//        addButton = findViewById(R.id.add_button)
//        nameEditText = findViewById(R.id.name_edit_text)
//        priceEditText = findViewById(R.id.price_edit_text)
//        nameEditText!!.addTextChangedListener(this)
//        priceEditText!!.addTextChangedListener(this)
//        addButton!!.setOnClickListener(View.OnClickListener {
//            putItemToInternet(
//                getSharedPreferences(
//                    getString(R.string.app_name),
//                    0
//                )
//            )
//        })
//        if (intent.getIntExtra("currentPosition", 0) == 1) {
//            inputColor = resources.getColor(R.color.apple_green)
//            type = "income"
//        } else {
//            inputColor = resources.getColor(R.color.dark_sky_blue)
//            type = "expense"
//        }
//        nameEditText!!.setTextColor(inputColor)
//        priceEditText!!.setTextColor(inputColor)
//        setAddButtonStatus()
    }

//    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//    override fun afterTextChanged(s: Editable) {
//        setAddButtonStatus()
//    }

//    private fun setAddButtonStatus() {
//        if (nameEditText!!.text.toString() != "" && priceEditText!!.text.toString() != "") {
//            addButton!!.setTextColor(inputColor)
//            addButton!!.isClickable = true
//            addButton!!.compoundDrawables[0].setTint(inputColor)
//        } else {
//            addButton!!.setTextColor(resources.getColor(R.color.white_three))
//            addButton!!.isClickable = false
//            addButton!!.compoundDrawables[0].setTint(resources.getColor(R.color.white_three))
//        }
//    }

    private fun putItemToInternet(sharedPreferences: SharedPreferences) {
        val type = when (transactionType) {
            INCOME -> "income"
            EXPENSE -> "expense"
            else -> return
        }
        val authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "")
        val disposable = (application as LoftApp).itemsAPI
            .postItems(
                priceEditText!!.text.toString(),
                nameEditText!!.text.toString(),
                type,
                authToken
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { finish() },
                { Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show() }
            )
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}