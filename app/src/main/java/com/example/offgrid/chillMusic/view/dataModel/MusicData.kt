package com.example.offgrid.chillMusic.view.dataModel

import android.net.Uri
import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongData(
    val name:String= "OffGrid" ,
    val extraData:String?,
    val duration:Long?,
    val uriSong:Uri?=null,
    val uriSongImage:Uri?=null
    ):Parcelable{

    }
