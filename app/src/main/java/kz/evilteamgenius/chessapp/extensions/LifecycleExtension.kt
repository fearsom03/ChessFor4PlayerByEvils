package kz.evilteamgenius.chessapp.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kz.evilteamgenius.chessapp.R

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

