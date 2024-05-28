package com.example.offgrid.chillMusic.view.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offgrid.R
import com.example.offgrid.baseUtility.Constants
import com.example.offgrid.baseUtility.gone
import com.example.offgrid.baseUtility.visible
import com.example.offgrid.chillMusic.adapter.SongListAdapter
import com.example.offgrid.chillMusic.result.ProcessResult
import com.example.offgrid.chillMusic.view.dataModel.SongData
import com.example.offgrid.chillMusic.view.fragment.MusicListFragment
import com.example.offgrid.chillMusic.view.fragment.MusicPlayingFragment
import com.example.offgrid.chillMusic.view.viewmodel.MusicHomeActivityViewModel
import com.example.offgrid.databinding.MusicHomeActivityLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicHomeActivity:AppCompatActivity() {
    companion object{
        private const val MUSIC_PLAYING_FRAGMENT="MUSIC_PLAYING_FRAGMENT"
        private const val MUSIC_PLAYING_FRAGMENT1="MUSIC_PLAYING_FRAGMENT1"
    }

    private lateinit var binding:MusicHomeActivityLayoutBinding
    private val viewModel:MusicHomeActivityViewModel by lazy {
        ViewModelProvider(this)[MusicHomeActivityViewModel::class.java]
    }

    private val STORAGE_PERMISSION_REQUEST_CODE = 1001
    val backPressCallBack = object :OnBackPressedCallback(false){
        override fun handleOnBackPressed() {
            if(supportFragmentManager.backStackEntryCount>1){
                supportFragmentManager.popBackStack()
            }else{
                finish()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MusicHomeActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Music List"
//        window.setStatusBarColor(getColor(R.color.color_e6ffff))
        observe()
        getUserStoragePermission()
        onBackPressedDispatcher.addCallback(this,backPressCallBack)
        backPressCallBack.isEnabled= true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && (grantResults[0]==PackageManager.PERMISSION_GRANTED || grantResults[0]==PackageManager.PERMISSION_DENIED)){
                getUserStoragePermission()
            }
            else{
                Toast.makeText(this,"Q Bro Gaana nhi Sunoge!" , Toast.LENGTH_LONG).show()
            }
        }
    }



    fun getUserStoragePermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) ==PackageManager.PERMISSION_GRANTED){
            addMusicListFragment()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    fun observe(){
        viewModel.playPauseSong.observe(this){
            addMusicPlayingFragment()
        }

    }


    fun addMusicPlayingFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.flFragmentContainer.id,MusicPlayingFragment.newInstance())
        transaction.addToBackStack(MUSIC_PLAYING_FRAGMENT)
        transaction.commit()
    }

    fun addMusicListFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.flFragmentContainer.id, MusicListFragment.newInstance())
        transaction.addToBackStack(MUSIC_PLAYING_FRAGMENT1)
        transaction.commit()
    }




}