package kz.evilteamgenius.chessapp.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import kz.evilteamgenius.chessapp.service.MusicService


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.inVisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.startMusicAction() {
    val action = Intent(this, MusicService::class.java)
    startService(action)
}