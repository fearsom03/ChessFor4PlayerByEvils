package kz.evilteamgenius.chessapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.evilteamgenius.chessapp.repository.CommonRepo

class GameViewModel : ViewModel() {
    private var musicIsPlaying = MutableLiveData<Boolean>()
    private var internetIsOk = MutableLiveData<Boolean>()
    private var commonChatRepository = CommonRepo()
    private var nextSong = MutableLiveData<Boolean>()
    private var prevSong = MutableLiveData<Boolean>()
    private var selectedStyle = MutableLiveData<Int>()

    init {
        musicIsPlaying.value = true
        internetIsOk.value = true
        nextSong.value = false
        prevSong.value = false
        selectedStyle.value = 0
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

    fun getNextSong(): LiveData<Boolean> {
        return nextSong
    }

    fun getPrevSong(): LiveData<Boolean> {
        return prevSong
    }

    fun checkVersion(): LiveData<Boolean> {
        return commonChatRepository.checkVersion()
    }

    fun startNextMusic() {
        nextSong.value = true
    }

    fun startPrevMusic() {
        prevSong.value = true
    }

    fun setSelectedStyle(position: Int) {
        selectedStyle.value = position
    }

    fun getSelectedStyle(): LiveData<Int> {
        return selectedStyle
    }

    fun defaultValChanger() {
        nextSong.value = false
        prevSong.value = false
    }
}