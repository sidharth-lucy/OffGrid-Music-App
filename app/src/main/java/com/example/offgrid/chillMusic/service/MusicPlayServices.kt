package com.example.offgrid.chillMusic.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat.StopForegroundFlags
import com.example.offgrid.R
import com.example.offgrid.baseUtility.Constants
import com.example.offgrid.chillMusic.view.dataModel.SongData

class MusicPlayServices():Service()  {
    private lateinit var mediaPlayer:MediaPlayer
    private lateinit var mediaSession:MediaSessionCompat
    private val binder = MusicBinder()
    var mediaPlayerCreated = false
    var serviceListener : MusicServicesCallBack?  = null

    inner class MusicBinder:Binder(){
        fun getServices(mServiceListener : MusicServicesCallBack):MusicPlayServices {
            serviceListener = mServiceListener
            return this@MusicPlayServices
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(this,"My Music")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    fun createMediaPlayer(song: Uri){
        if(mediaPlayer!=null){
            mediaPlayer.reset()
        }
        mediaPlayer.setDataSource(this,song)
        mediaPlayer.setOnPreparedListener {
            mediaPlayerCreated= true
            serviceListener?.onServicesBinds(true)
        }
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            mediaPlayerCreated= false
            serviceListener?.onServicesBinds(false)
            return@setOnErrorListener false
        }
        mediaPlayer.setOnCompletionListener {
            serviceListener?.onPlaybackEnds()
        }
        mediaPlayer.prepareAsync()

    }

    fun playSong(){
        if(!mediaPlayer.isPlaying){
            mediaPlayer.start()
        }
    }

    fun stopSong(){
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
    }
    fun pauseSong(){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
    }

    fun goInLoop(loopSong:Boolean){
        mediaPlayer.isLooping = loopSong
    }

    fun getMusicDuration():Int?{
        if(mediaPlayer!=null && mediaPlayerCreated){
            return mediaPlayer.duration
        }
        return null
    }

    fun getCurrentPosition():Int?{
        if(mediaPlayer!=null && mediaPlayerCreated){
            return mediaPlayer.currentPosition
        }
        return null
    }

    fun setMediaPlayerSeekPosition(position:Int){
        if(mediaPlayer!=null && mediaPlayerCreated){
            mediaPlayer.seekTo(position)
        }
    }

    fun showNotification(music:SongData){
        val notification = NotificationCompat.Builder(this,Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(music.name)
            .setContentText(music.extraData ?: "Sidh")
            .setSmallIcon(R.drawable.music_player)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.music_playing))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
//            .addAction(R.drawable.ic_previous_button, "previous",null)
//            .addAction(R.drawable.ic_pause_button, "play_pause",null)
//            .addAction(R.drawable.ic_next_button, "next",null)
//            .addAction(R.drawable.ic_cross_icon, "previous",null)
            .build()
        startForeground(Constants.FOREGROUND_SERVICE_ID, notification)
    }

}