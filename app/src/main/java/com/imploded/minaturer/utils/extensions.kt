package com.imploded.minaturer.utils

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun String.toColor(): Int {
    return Color.parseColor(this)
}

fun String.filteredStops(stopId: String): List<Map<String, String>> {
    var allFilters = Gson().fromJson<Map<String, Map<String, String>>>(this)
    return allFilters.filter { p -> p.key.equals(stopId) }.map { p -> p.value }
}

fun String.etaTime() : String {
    return if (this.isEmpty()) "" else "eta: $this"
}