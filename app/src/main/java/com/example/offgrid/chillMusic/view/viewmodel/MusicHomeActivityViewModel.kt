package com.example.offgrid.chillMusic.view.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offgrid.DI.AppModuleDI
import com.example.offgrid.R
import com.example.offgrid.chillMusic.result.ProcessResult
import com.example.offgrid.chillMusic.service.MusicPlayServices
import com.example.offgrid.chillMusic.view.dataModel.SongData
import com.example.offgrid.chillMusic.view.utils.MusicUtilityFunc

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicHomeActivityViewModel @Inject constructor(
    private val musicUtilityFunc:MusicUtilityFunc,
    @AppModuleDI.DefaultDispatcher private val defaultDispatcher:CoroutineDispatcher

):ViewModel() {

    var musicListGrabbed:List<SongData>? = null
    var activeMusic:SongData?= null
    var activeMusicIndex:Int?= null
    var musicPlaying:Boolean= false

    var musicService: MusicPlayServices? = null
    var isBound = false


    private var _songListLiveData:MutableLiveData<ProcessResult<List<SongData>,String>> = MutableLiveData()
    val songListLiveData:LiveData<ProcessResult<List<SongData>,String>> = _songListLiveData

    private var _loader:MutableLiveData<Pair<Boolean,String>> = MutableLiveData()
    val loader:LiveData<Pair<Boolean,String>> = _loader

    private var _playPauseSong:MutableLiveData<Pair<SongData,Boolean>>  = MutableLiveData()
    val playPauseSong:MutableLiveData<Pair<SongData,Boolean>> = _playPauseSong

    private var _playNextSong:MutableLiveData<Pair<SongData?,String>>  = MutableLiveData()
    val playNextSong:MutableLiveData<Pair<SongData?,String>> = _playNextSong



    fun getAllSongListFromStorage(context: Context){
        viewModelScope.launch(defaultDispatcher) {
            _loader.postValue(Pair(true,"Loading Song"))
            val musicList = musicListGrabbed ?: musicUtilityFunc.getAllMusicList(context)

            if(!musicList.isNullOrEmpty()){
                _loader.postValue(Pair(false,""))
                musicListGrabbed= musicList
                _songListLiveData.postValue(ProcessResult.Success(musicList))
            }else{
                _loader.postValue(Pair(false,"No any song available"))
                _songListLiveData.postValue(ProcessResult.Failed("No any Music found"))
            }

        }
    }

    fun openSongPlayingFragment(activeSong:SongData,musicPlayingPause:Boolean,songIndex:Int){
        activeMusic= activeSong
        activeMusicIndex= songIndex
        musicPlaying= musicPlayingPause
        _playPauseSong.postValue(Pair(activeSong,musicPlayingPause))
    }

    fun handleNextButtonClick(isNext:Boolean):Boolean{
        if(isNext && activeMusicIndex!=null && musicListGrabbed!=null && (activeMusicIndex!!+1< musicListGrabbed!!.size)){
            musicListGrabbed?.let {
                    activeMusic = it[activeMusicIndex!! +1]
                    activeMusicIndex = activeMusicIndex!! + 1
                    musicPlaying= true
                    _playNextSong.postValue(Pair(activeMusic,""))
                return true
            }
        }
        else if(!isNext && activeMusicIndex!=null && musicListGrabbed!=null && (activeMusicIndex!! -1>=0)){
            musicListGrabbed?.let {
                activeMusic = it[activeMusicIndex!! -1]
                activeMusicIndex = activeMusicIndex!! - 1
                musicPlaying= true
                _playNextSong.postValue(Pair(activeMusic,""))
                return true
            }
        }
        else{
            _playNextSong.postValue(Pair(null,"No more songs are there!"))
        }
        return false
    }



}