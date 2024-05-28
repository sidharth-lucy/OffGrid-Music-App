package com.example.offgrid.chillMusic.view.utils.impl

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.offgrid.DI.AppModuleDI

import com.example.offgrid.R
import com.example.offgrid.chillMusic.view.dataModel.SongData
import com.example.offgrid.chillMusic.view.utils.MusicUtilityFunc
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicUtilityFuncImpl @Inject constructor(
//    @AppModuleDI.IoDispatcher private val ioDispatcher:CoroutineDispatcher
): MusicUtilityFunc {


    override suspend fun getAllMusicList(context: Context): List<SongData>? {
        return withContext(Dispatchers.IO){
            val songList = mutableListOf<SongData>()
            var mediaUrl = mutableListOf<Uri>()
            val contentResolver:ContentResolver= context.contentResolver

            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.Q){
                val mediaUrl1:Uri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                val mediaUrl2:Uri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_INTERNAL)
                mediaUrl.add(mediaUrl1)
                mediaUrl.add(mediaUrl2)
            }else{
                val mediaUrl1:Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val mediaUrl2:Uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
                mediaUrl.add(mediaUrl1)
                mediaUrl.add(mediaUrl2)
            }
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ARTIST,
                MediaStore.Audio.Media.ALBUM_ID
            )
            val sortOrder= MediaStore.Audio.Media.DATE_ADDED+" DESC"
            val selection = "${MediaStore.Audio.Media.MIME_TYPE}=? AND ${MediaStore.Audio.Media.DISPLAY_NAME} LIKE ?"
            val selectionArgs = arrayOf("audio/mpeg", "%.mp3")
            mediaUrl.forEach {
                val cursor = contentResolver.query(it,projection,selection,selectionArgs,sortOrder)
                cursor?.use {
                    val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                    val nameColumn= it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                    val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                    val artistNameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST)
                    val albumIdColumn= it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)


                    while (cursor.moveToNext()){
                        val id = cursor.getLong(idColumn)
                        var name = cursor.getString(nameColumn)
                        val duration = cursor.getLong(durationColumn)
                        val artistName = cursor.getString(artistNameColumn)
                        val albumId = cursor.getLong(albumIdColumn)

                        val uriMusic = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id)
                        val albumArtWork = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId)

                        name?.let {
                            name = name.substring(0, it.lastIndexOf("."))
                        }

                        if(uriMusic!=null){
                            val songData = SongData(
                                name,
                                artistName,
                                duration,
                                uriMusic,
                                albumArtWork,
                            )
                            songList.add(songData)
                        }

                    }

                }
            }
            return@withContext songList
        }
    }




}