package com.example.testesdkmercadopago

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.NumberFormat
import java.util.Locale

object Utils {

    fun applyCurrencyMask(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    editText.removeTextChangedListener(this)

                    val cleanString = s.toString().replace("[R$,.\\s]".toRegex(), "")
                    val parsed = if (cleanString.isEmpty()) 0.0 else cleanString.toDouble()
                    val formatted = NumberFormat
                        .getCurrencyInstance(Locale("pt", "BR"))
                        .format(parsed / 100)

                    current = formatted
                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                    editText.addTextChangedListener(this)
                }
            }
        })
    }

    fun removeCurrencyMask(value: String): String {
        return value.replace(Regex("[^\\d.]"), "")
    }

}