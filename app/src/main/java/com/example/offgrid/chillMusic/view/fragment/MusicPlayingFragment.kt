package com.example.offgrid.chillMusic.view.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.offgrid.chillMusic.view.dataModel.SongData
import com.example.offgrid.chillMusic.view.viewmodel.MusicHomeActivityViewModel
import com.example.offgrid.chillMusic.view.viewmodel.MusicPlayingFragmentViewModel
import com.example.offgrid.databinding.MusicPlayingFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.offgrid.R
import com.example.offgrid.baseUtility.showToast
import com.example.offgrid.chillMusic.service.MusicPlayServices
import com.example.offgrid.chillMusic.service.MusicServicesCallBack



@AndroidEntryPoint
class MusicPlayingFragment: MusicServicesCallBack,Fragment(),ServiceConnection {

    companion object{
        fun newInstance():MusicPlayingFragment{
            return MusicPlayingFragment().also {
                val bundle = Bundle()
            }
        }
    }

    lateinit var binding: MusicPlayingFragmentLayoutBinding
    private val viewModel by viewModels<MusicHomeActivityViewModel>(ownerProducer={
        requireActivity()
    })

    private val fragmentViewModel by viewModels<MusicPlayingFragmentViewModel>(ownerProducer = {
        this
    })



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MusicPlayingFragmentLayoutBinding.inflate(layoutInflater)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(requireContext())
        onClickListener()
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(requireContext() , MusicPlayServices::class.java)
        requireContext().bindService(intent,this,Context.BIND_AUTO_CREATE)
        requireContext().startService(intent)
    }

    fun observe(context: Context) {
        viewModel.playNextSong.observe(requireActivity()) {
            if (it.first != null) {
                updateUi(it.first, context)
                playMusic(it.first!!,true)
            } else {
                showToast(context, it.second ?: "No more song!", Toast.LENGTH_LONG)
            }
        }
    }

    private fun handlePlayPauseUi(){
        if(viewModel.activeMusic!=null){
            if(viewModel.musicPlaying){
                binding.ivPlayPauseBtn.setImageResource(R.drawable.ic_pause_button)
                binding.lvLottieMusicPlay.playAnimation()
            }else{
                binding.ivPlayPauseBtn.setImageResource(R.drawable.ic_play_button)
                binding.lvLottieMusicPlay.pauseAnimation()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if(viewModel.isBound){
            requireContext().unbindService(this)
            viewModel.isBound= false
        }
    }



    fun onClickListener(){
        binding.ivPlayPauseBtn.setOnClickListener {
            handelPlayPauseClick(viewModel.musicPlaying,viewModel.activeMusic)
        }
        binding.ivNextBtn.setOnClickListener {
            viewModel.handleNextButtonClick(true)
        }
        binding.ivPreviousBtn.setOnClickListener {
            viewModel.handleNextButtonClick(false)
        }

        binding.sbMusicBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser && viewModel.musicService?.mediaPlayerCreated==true){
                    viewModel.musicService?.setMediaPlayerSeekPosition(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun handelPlayPauseClick(musicPlaying:Boolean , songData: SongData?){
        if(musicPlaying){
            viewModel.musicPlaying = false
            binding.ivPlayPauseBtn.setImageResource(R.drawable.ic_play_button)
            binding.lvLottieMusicPlay.pauseAnimation()
            pauseMusic()
        }else{
            viewModel.musicPlaying = true
            binding.ivPlayPauseBtn.setImageResource(R.drawable.ic_pause_button)
            binding.lvLottieMusicPlay.playAnimation()
            songData?.let {
                playMusic(it)
            }
        }
    }

    private fun playMusic(songData: SongData,fromNext:Boolean=false){
        binding.lvLottieMusicPlay.pauseAnimation()
        if(viewModel.musicService!=null && viewModel.isBound && viewModel.musicService?.mediaPlayerCreated==true && !fromNext){
            viewModel.musicService?.playSong()
            handlePlayPauseUi()
        }
        else if(viewModel.musicService!=null){
            songData.uriSong?.let {
                viewModel.musicService?.createMediaPlayer(it)
                updateUi(viewModel.activeMusic,requireContext())
            }
        }
        else{
            Log.d("Service Not Created","Service Not Created")
        }
    }

    private fun pauseMusic(){
        viewModel.musicService?.pauseSong()
    }



    private fun updateUi(data:SongData?, context:Context){
        if(data==null){
            return
        }
        if(!data.name.isNullOrEmpty()){
            binding.tvMusicName.text = data.name + (data.extraData ?: "")
        }
        if(data.uriSongImage!=null){
            Glide.with(context).load(data.uriSongImage).error(R.drawable.music_playing).into(binding.ivSongImage)
        }else{
            binding.ivSongImage.setImageResource(R.drawable.music_playing)
        }
    }

    override fun onServicesBinds(binds: Boolean) {
        if(binds){
            viewModel.musicService?.playSong()
            initilizeSeekbar()
            if(viewModel.activeMusic!=null){
                viewModel.musicService?.showNotification(viewModel.activeMusic!!)
            }
        }else{
            viewModel.musicService?.stopSong()
        }
        handlePlayPauseUi()
     }

    override fun onPlaybackEnds() {
        if(!viewModel.handleNextButtonClick(true)){
            pauseMusic()
            viewModel.musicPlaying=false
            handlePlayPauseUi()
        }
    }

    private fun initilizeSeekbar(){
        var duration = viewModel.musicService?.getMusicDuration()
        if(duration!=null){
            binding.sbMusicBar.max = duration
            val handler = Handler()
            handler.postDelayed(object : Runnable{
                override fun run() {
                    try {
                        val currPosition = viewModel.musicService?.getCurrentPosition()
                        if(currPosition!=null){
                            binding.sbMusicBar.progress = currPosition
                        }
                        handler.postDelayed(this,1000)
                    }
                    catch (e:Exception){
                        Log.d("SeekBar", "Seekbar Error")
                    }
                }

            },0)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder =service as  MusicPlayServices.MusicBinder
        viewModel.musicService = binder.getServices(this@MusicPlayingFragment)
        viewModel.isBound = true
        if (viewModel.musicPlaying) {
            viewModel.activeMusic?.let {
                playMusic(it)
                handlePlayPauseUi()
            }
        } else {

        }

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        viewModel.isBound = false
        viewModel.musicService= null
    }


//    @SuppressLint("ClickableViewAccessibility")
//    override fun getView(): View? {
//        val view = super.getView()
//        view?.setOnTouchListener { v, event ->
//            return@setOnTouchListener true
//        }
//        return super.getView()
//    }


}