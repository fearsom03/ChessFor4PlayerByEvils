package kz.evilteamgenius.chessapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private var musicIsPlaying = MutableLiveData<Boolean>()

    init {
        musicIsPlaying.value = true
    }

    fun setMusic(boolean: Boolean) {
        musicIsPlaying.value = boolean
    }

    fun getMusicValue(): MutableLiveData<Boolean> {
        return musicIsPlaying
    }
}