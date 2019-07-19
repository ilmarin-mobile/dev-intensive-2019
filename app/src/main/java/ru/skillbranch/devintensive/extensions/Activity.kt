package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    if (getCurrentFocus() == null) return

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.run { hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0) }
}