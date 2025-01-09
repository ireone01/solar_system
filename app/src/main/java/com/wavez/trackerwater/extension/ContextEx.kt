package com.wavez.trackerwater.extension

import android.app.Activity
import android.app.LocaleManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Build
import android.os.LocaleList
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import java.util.Locale

fun Context.getDisplayMetrics(): DisplayMetrics {
    return DisplayMetrics().apply {
        getSystemService<DisplayManager>()?.getDisplay(Display.DEFAULT_DISPLAY)
    }
}


fun Context.goToSettingNetwork() {
    try {
        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    } catch (ignored: ActivityNotFoundException) {
    }
}


fun Context.dpToPx(dp: Float) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics
).toInt()

fun Context.setLocaleSupport(c: String) {
    val ca = c.split("-")
    val locale = Locale(ca.firstOrNull() ?: return, ca.getOrNull(1) ?: "")
    Locale.setDefault(locale)
    val tag = locale.toLanguageTag()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(tag)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(tag)
        )
    }
}

fun Fragment.toastMsg(msg: Int) =
    Toast.makeText(requireActivity(), this.getString(msg), Toast.LENGTH_SHORT).show()

fun Context.getDisplayWidth() = getDisplayMetrics().widthPixels

fun Context.getDisplayHeight() = getDisplayMetrics().heightPixels

fun Context.toastMsg(msg: Int) =
    Toast.makeText(this, this.getString(msg), Toast.LENGTH_SHORT).show()

fun Context.toastMsg(msg: String) =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

@ColorInt
fun Context.color(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

fun Context.drawable(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

inline fun <reified VM : ViewModel> Fragment.fragmentViewModels() =
    viewModels<VM>({ requireParentFragment() })


fun Context.tintedDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable? {
    val tint: Int = color(colorId)
    val drawable = drawable(drawableId)
    drawable?.mutate()
    drawable?.let {
        it.mutate()
        DrawableCompat.setTint(it, tint)
    }
    return drawable
}

fun Context.string(@StringRes res: Int): String {
    return getString(res)
}

fun Context.hideSoftKeyboard(v: View) {
    val inputMethodManager: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
}

fun toastInFragment(context: Context, msg: Int) =
    Toast.makeText(context, context.getString(msg), Toast.LENGTH_SHORT).show()


fun Context.showSoftKeyboard(v: View) {
    try {
        v.post {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        }
    } catch (e: Exception) {

    }
}


fun AppCompatActivity.hideSoftKeyboard() {
    val inputMethodManager = getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            0
        )
    }
}

fun openSettings(context: Context) {
    try {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:" + context.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.d("naoh", "openSettings: error")
        e.printStackTrace()
    }
}

fun Context.linkAppStore() {
    val uri: Uri = Uri.parse("market://details?id=${this.packageName}")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    try {
        this.startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=${this.packageName}")
            )
        )
    }
}


fun Fragment.hideSoftKeyboard() {
    val inputMethodManager = requireActivity().getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(
            requireActivity().currentFocus!!.windowToken,
            0
        )
    }
}


fun Fragment.showChildDialog(dialogFragment: DialogFragment) {
    lifecycleScope.launchWhenResumed {
        if (childFragmentManager.findFragmentByTag(dialogFragment::class.simpleName) != null) {
            return@launchWhenResumed
        }
        dialogFragment.show(childFragmentManager, dialogFragment.javaClass.simpleName)
    }
}
