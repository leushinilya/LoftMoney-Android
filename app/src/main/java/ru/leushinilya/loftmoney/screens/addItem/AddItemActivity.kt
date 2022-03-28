package ru.leushinilya.loftmoney.screens.addItem

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.R

class AddItemActivity : AppCompatActivity(), TextWatcher {
    var addButton: Button? = null
    var nameEditText: EditText? = null
    var priceEditText: EditText? = null
    var inputColor = 0
    var type = "expense"
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        addButton = findViewById(R.id.add_button)
        nameEditText = findViewById(R.id.name_edit_text)
        priceEditText = findViewById(R.id.price_edit_text)
        nameEditText!!.addTextChangedListener(this)
        priceEditText!!.addTextChangedListener(this)
        addButton!!.setOnClickListener(View.OnClickListener {
            putItemToInternet(
                getSharedPreferences(
                    getString(R.string.app_name),
                    0
                )
            )
        })
        if (intent.getIntExtra("currentPosition", 0) == 1) {
            inputColor = resources.getColor(R.color.apple_green)
            type = "income"
        } else {
            inputColor = resources.getColor(R.color.dark_sky_blue)
            type = "expense"
        }
        nameEditText!!.setTextColor(inputColor)
        priceEditText!!.setTextColor(inputColor)
        setAddButtonStatus()
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        setAddButtonStatus()
    }

    private fun setAddButtonStatus() {
        if (nameEditText!!.text.toString() != "" && priceEditText!!.text.toString() != "") {
            addButton!!.setTextColor(inputColor)
            addButton!!.isClickable = true
            addButton!!.compoundDrawables[0].setTint(inputColor)
        } else {
            addButton!!.setTextColor(resources.getColor(R.color.white_three))
            addButton!!.isClickable = false
            addButton!!.compoundDrawables[0].setTint(resources.getColor(R.color.white_three))
        }
    }

    private fun putItemToInternet(sharedPreferences: SharedPreferences) {
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