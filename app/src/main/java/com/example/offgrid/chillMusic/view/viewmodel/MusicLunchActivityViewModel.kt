package com.example.offgrid.chillMusic.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicLunchActivityViewModel @Inject constructor(): ViewModel() {

    private var _moveToNewPage= MutableLiveData<Boolean>()
    val moveToNewPage:MutableLiveData<Boolean> = _moveToNewPage

    fun makeDelay(timeInMilli: Long){
        viewModelScope.launch {
            delay(timeInMilli)
            _moveToNewPage.postValue(true)
        }
    }

}