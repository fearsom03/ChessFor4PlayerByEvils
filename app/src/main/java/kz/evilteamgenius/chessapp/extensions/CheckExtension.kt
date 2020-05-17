package kz.evilteamgenius.chessapp.extensions

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import java.util.regex.Pattern

fun isValidEmailAddress(email: String): Boolean {
    return try {
        val ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        m.matches()
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun checkServer(): Boolean {
    return false
}

fun checkInternetKt(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Context.checkInternet(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Context.getUsername(): String? {
    val preferences: SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    return preferences.getString("username", null)
}