package kz.evilteamgenius.chessapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.evilteamgenius.chessapp.repository.CommonRepo

class GameViewModel : ViewModel() {
    private var musicIsPlaying = MutableLiveData<Boolean>()
    private var internetIsOk = MutableLiveData<Boolean>()
    private var commonChatRepository = CommonRepo()
    init {
        musicIsPlaying.value = true
        internetIsOk.value = true
    }

    fun setMusic(boolean: Boolean) {
        musicIsPlaying.value = boolean
    }

    fun getMusicValue(): MutableLiveData<Boolean> {
        return musicIsPlaying
    }

    fun getInternetCheck(): MutableLiveData<Boolean> {
        return internetIsOk
    }

    fun setInternetCheck(boolean: Boolean) {
        internetIsOk.value = boolean
    }

    fun checkVersion(): MutableLiveData<Boolean> {
        return commonChatRepository.checkVersion()
    }
}