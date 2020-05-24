package kz.evilteamgenius.chessapp.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kz.evilteamgenius.chessapp.BuildConfig
import timber.log.Timber
import java.io.Serializable

class CommonRepo {
    var db = FirebaseFirestore.getInstance()
    private val versionIsOK = MutableLiveData<Boolean>()

    init {
        versionIsOK.value = true
    }


    fun checkVersion(): MutableLiveData<Boolean> {
        db.collection("VersionOfApp")
                .addSnapshotListener { documentSnapshot, e ->
                    if (e != null || documentSnapshot == null) {
                        Timber.e("ERROR OF VERSION")
                        return@addSnapshotListener
                    }
                    val versionOfApp: VersionOfApp? = documentSnapshot.documents[0].toObject(VersionOfApp::class.java)
                    if (versionOfApp != null &&
                            BuildConfig.VERSION_CODE
                            < versionOfApp.versionCode) {
                        versionIsOK.setValue(false)
                    } else {
                        versionIsOK.setValue(true)
                    }
                }
        return versionIsOK
    }
}


class VersionOfApp : Serializable {
    var versionCode = 1
}
