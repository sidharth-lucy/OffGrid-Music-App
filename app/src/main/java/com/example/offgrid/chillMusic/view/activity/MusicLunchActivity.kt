package com.example.offgrid.chillMusic.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.offgrid.chillMusic.view.utils.MusicNavigationUtility
import com.example.offgrid.chillMusic.view.viewmodel.MusicLunchActivityViewModel
import com.example.offgrid.databinding.MusicappLunchActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MusicLunchActivity:AppCompatActivity() {

    lateinit var binding: MusicappLunchActivityBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[MusicLunchActivityViewModel::class.java]
    }

    @Inject
    lateinit var musicNavigationUtility: MusicNavigationUtility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MusicappLunchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        observe()
        viewModel.makeDelay(2000)
    }

    private fun observe(){
        viewModel.moveToNewPage.observe(this){
            if(it){
                musicNavigationUtility.goToHomeActivity(this)
                finish()
            }
        }
    }

}
