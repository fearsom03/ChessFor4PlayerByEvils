package kz.evilteamgenius.chessapp.extensions

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kz.evilteamgenius.chessapp.R
import kz.evilteamgenius.chessapp.database.asyncTasksDB.AddGameAsyncTask
import kz.evilteamgenius.chessapp.database.entitys.GameEntity

inline fun <T> LifecycleOwner.observeLiveData(data: LiveData<T>, crossinline onChanged: (T) -> Unit) {
    data.removeObservers(this)
    data.observe(this, Observer {
        it?.let { value -> onChanged(value) }
    })
}

fun Fragment.replaceFragment(fragment: Fragment) {
    val tr: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
    tr.replace(R.id.frame, fragment)
    tr.commit()
}

fun Fragment.getBackFragment(fragment: Fragment) {
    val tr: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
    tr.replace(R.id.frame, fragment)
    tr.commitNow()
}

fun Context.getToken(): String {
    val preferences: SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    return preferences.getString("token", "").toString()
}

private fun insertGameIntoDatabase(game: GameEntity, context: Context) {
    val addGameAsyncTask = AddGameAsyncTask(context)
    addGameAsyncTask.execute(game)
}


