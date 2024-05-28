package com.example.offgrid.chillMusic.view.utils.impl

import android.content.Context
import android.content.Intent
import com.example.offgrid.chillMusic.view.activity.MusicHomeActivity
import com.example.offgrid.chillMusic.view.dataModel.SongData
import com.example.offgrid.chillMusic.view.utils.MusicNavigationUtility
import javax.inject.Singleton

@Singleton
class MusicNavigationUtilityImpl:MusicNavigationUtility {
    override fun goToHomeActivity(context: Context) {
        val intent = Intent(context,MusicHomeActivity::class.java)
        context.startActivity(intent)
    }


}