package com.example.offgrid.chillMusic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.offgrid.R
import com.example.offgrid.baseUtility.getMilliSecToTime
import com.example.offgrid.chillMusic.view.dataModel.SongData
import com.example.offgrid.databinding.SongItemLayoutBinding

class SongListAdapter(
    private val context: Context,
    private var data:List<SongData>,
    private val onSongClicked:(data:SongData,haveToPlay:Boolean,songIndex:Int)->Unit
    ):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var activeSong:SongItemLayoutBinding? = null
    var activeSongId:Int?=null
    inner class SongViewHolder(var binding:SongItemLayoutBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(song:SongData,position: Int){
            binding.let {itemBinding->
                itemBinding.tvSongName.text = song.name

                var extraData = ""
                if(song.duration!=null){
                    extraData+= getMilliSecToTime(song.duration)
                }
                if(!song.extraData.isNullOrEmpty()){
                    extraData+= song.extraData
                }
                itemBinding.tvSongTimeDetail.text= extraData
                if(song.uriSongImage!=null){
                    itemBinding.ivMusicIcon.setImageURI(song.uriSongImage)
                    Glide.with(context).load(song.uriSongImage).error(assignSongIcon(position)).into(itemBinding.ivMusicIcon)
                }else{
                    itemBinding.ivMusicIcon.setImageResource(assignSongIcon(position))
                }


                itemBinding.clSongContainer.setOnClickListener{
                    if(activeSongId==position){
                        //pause song
                        onSongClicked(song,false,position)
                        activeSong?.clSongContainer?.setBackgroundResource(R.drawable.song_list_item_border)
                        activeSongId=null
                        activeSong=null
                    }else{
                        onSongClicked(song,true,position)
                        activeSong?.clSongContainer?.setBackgroundResource(R.drawable.song_list_item_border)
                        it.setBackgroundResource(R.drawable.song_list_active_item)
                        activeSong = itemBinding
                        activeSongId= position
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SongViewHolder(SongItemLayoutBinding.inflate(LayoutInflater.from(parent.context) , parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SongViewHolder).bind(data[position],position)
    }

    fun assignSongIcon(position:Int):Int{
        return when(position%7){
            0->{R.drawable.music_dancing_disco_1}
            1->{R.drawable.music_dancing_disco_2}
            2->{R.drawable.music_dancing_disco_3}
            3->{R.drawable.music_dancing_disco_4}
            4->{R.drawable.music_dancing_disco_5}
            5->{R.drawable.music_dancing_disco_6}
            6->{R.drawable.music_dancing_disco_7}
            else -> {R.drawable.music_dancing_disco_1}
        }
    }
}