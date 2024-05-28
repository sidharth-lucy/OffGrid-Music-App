package com.example.offgrid.chillMusic.view.utils

import android.content.Context
import android.net.Uri
import com.example.offgrid.chillMusic.view.dataModel.SongData

interface MusicUtilityFunc {

    suspend fun getAllMusicList(context: Context):List<SongData>?
}