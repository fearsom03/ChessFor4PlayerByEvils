package kz.evilteamgenius.chessapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.evilteamgenius.chessapp.repository.CommonRepo

class GameViewModel : ViewModel() {
    private var musicIsPlaying = MutableLiveData<Boolean>()
    private var internetIsOk = MutableLiveData<Boolean>()
    private var commonChatRepository = CommonRepo()
    private var songIdentifier = MutableLiveData<Int>()

    init {
        musicIsPlaying.value = true
        internetIsOk.value = true
        songIdentifier.value = 0
    }

    fun setMusicIsPlaying(boolean: Boolean) {
        musicIsPlaying.value = boolean
    }

    fun getMusicValue(): LiveData<Boolean> {
        return musicIsPlaying
    }

    fun getInternetCheck(): LiveData<Boolean> {
        return internetIsOk
    }

    fun setInternetCheck(boolean: Boolean) {
        internetIsOk.value = boolean
    }

    fun checkVersion(): LiveData<Boolean> {
        return commonChatRepository.checkVersion()
    }

    fun startNextMusic() {
        songIdentifier.value?.plus(1)
    }

    fun startPrevMusic() {
        songIdentifier.value?.minus(1)
    }
}