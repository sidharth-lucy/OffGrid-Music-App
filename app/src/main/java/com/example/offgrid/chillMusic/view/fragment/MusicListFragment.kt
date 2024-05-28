package com.example.offgrid.chillMusic.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offgrid.baseUtility.gone
import com.example.offgrid.baseUtility.visible
import com.example.offgrid.chillMusic.adapter.SongListAdapter
import com.example.offgrid.chillMusic.result.ProcessResult
import com.example.offgrid.chillMusic.view.dataModel.SongData
import com.example.offgrid.chillMusic.view.viewmodel.MusicHomeActivityViewModel
import com.example.offgrid.databinding.MusicListFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.logging.Logger

@AndroidEntryPoint
class MusicListFragment:Fragment() {

    companion object{
        fun newInstance():MusicListFragment{
            return MusicListFragment().also {
                val bundle = Bundle()
            }
        }
    }
    private var adapter:SongListAdapter? =null

    lateinit var binding:MusicListFragmentLayoutBinding
    val viewModel by viewModels<MusicHomeActivityViewModel>(ownerProducer = {
        requireActivity()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MusicListFragmentLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         observe()
        viewModel.getAllSongListFromStorage(requireContext())

    }


    fun showAppSongList(data:List<SongData>,context: Context){
        if(adapter==null){
            adapter = SongListAdapter(context,data, onSongClicked = {songData,haveToplay,songIndex->
                viewModel.openSongPlayingFragment(songData,haveToplay,songIndex)
            })

        }
        binding.rvFrSongList.layoutManager = LinearLayoutManager(context)
        binding.rvFrSongList.adapter = adapter
        viewModel.activeMusicIndex?.let {index ->
            binding.rvFrSongList.scrollToPosition(index)

        }
    }

    fun observe(){
        viewModel.loader.observe(requireActivity()){
            if(it.first){
                binding.llLoaderItem.visible()
            }else{
                binding.llLoaderItem.gone()
            }
        }
        viewModel.songListLiveData.observe(requireActivity()){
            when(it){
                is ProcessResult.Success->{
                    showAppSongList(it.data,requireContext())
                }
                is ProcessResult.Failed->{
                    Toast.makeText(requireContext(),"Kuch Galat ho gya Bro!" , Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(requireContext(),"Kuch Galat ho gya Bro!" , Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}