package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.content.res.Configuration

fun Activity.hideKeyboard() {
    if (getCurrentFocus() == null) return

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.run { hideSoftInputFromWindow(getCurrentFocus()!!.getWindowToken(), 0) }
}

fun Activity.showKeyboard() {
    if (getCurrentFocus() == null) return

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.run { showSoftInput(getCurrentFocus(), 0) }
}

fun Activity.isKeyboardOpen(): Boolean {
    return currentKeyboardHeight() > 0
}

fun Activity.isKeyboardClosed(): Boolean {
    return currentKeyboardHeight() == 0
}

fun Activity.currentKeyboardHeight(): Int {

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val appHeight = appHeight()
    Log.i("height", "appHeight ${appHeight}")

    val viewHeight = rootViewHeight()
    Log.i("height", "viewHeight ${viewHeight}")

    return appHeight - viewHeight
}

fun Activity.appHeight(): Int {
    val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val realSize = Point().apply { display.getRealSize(this) }

    val navHeight = navigationBarHeight()
    val statusBarHeight = statusBarHeight()

    return realSize.y - navHeight - statusBarHeight
}

fun Activity.rootViewHeight(): Int {
    val rect1 = Rect()
    window.decorView.rootView.getWindowVisibleDisplayFrame(rect1)
    Log.i("height", "rootView.height = "+ window.decorView.rootView.height)
    return rect1.bottom-rect1.top
}

fun Activity.navigationBarHeight(): Int {
    var navigationBarHeight = 0
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        navigationBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    val orientation = getResources().getConfiguration().orientation

    return if (orientation == Configuration.ORIENTATION_PORTRAIT) navigationBarHeight else 0;
}

fun Activity.statusBarHeight(): Int {
    // status bar height
    var statusBarHeight = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = resources.getDimensionPixelSize(resourceId)
    }

    return statusBarHeight
}


