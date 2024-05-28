package com.example.offgrid.chillMusic.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MusicPlayingFragmentViewModel @Inject constructor():ViewModel() {

    private var _musicPlayServiceIsReady:MutableLiveData<Boolean> = MutableLiveData()
    val  musicPlayServiceIsReady:LiveData<Boolean> = _musicPlayServiceIsReady

    fun getServiceStatus(isReady:Boolean){
        _musicPlayServiceIsReady.postValue(isReady)
    }

}