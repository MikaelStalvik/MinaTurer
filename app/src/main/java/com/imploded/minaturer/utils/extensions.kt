package com.imploded.minaturer.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorRes
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun MenuItem.tintMenuIcon(context: Context, @ColorRes color: Int) {
    val normalDrawable = this.icon
    val wrapDrawable = DrawableCompat.wrap(normalDrawable)
    DrawableCompat.setTint(wrapDrawable, context.resources.getColor(color))

    this.icon = wrapDrawable
}

fun String.etaTime(originalTime: String) : String {
    if (this.equals(originalTime, true)) return ""
    return if (this.isEmpty()) "" else "eta: $this"
}

fun Activity.inputMethodManager() : InputMethodManager {
    return this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}
fun View.hideKeyboard(inputMethodManager: InputMethodManager) {
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}
