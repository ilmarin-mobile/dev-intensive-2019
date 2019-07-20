package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
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
    return isKeyboardVisible()
}

fun Activity.isKeyboardClosed(): Boolean {
    return !isKeyboardVisible()
}

fun Activity.isKeyboardVisible(): Boolean {
    val orientation = getResources().getConfiguration().orientation

    if (orientation == Configuration.ORIENTATION_PORTRAIT)
        return currentKeyboardHeight() != 0
    else {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isFullscreenMode
    };
}

fun Activity.currentKeyboardHeight(): Int {
    return appHeight() - rootViewHeight()
}

fun Activity.appHeight(): Int {
    val display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val realSize = Point().apply { display.getRealSize(this) }

    val navHeight = navigationBarHeight()
    val statusBarHeight = statusBarHeight()

    return realSize.y - navHeight - statusBarHeight
}

fun Activity.rootViewHeight(): Int {
    val rect = Rect().apply { window.decorView.rootView.getWindowVisibleDisplayFrame(this) }
    return rect.bottom-rect.top
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


