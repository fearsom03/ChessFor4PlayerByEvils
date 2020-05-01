package kz.evilteamgenius.chessapp.extensions

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

fun checkInternet(): Boolean {
    return false
}