package com.example.offgrid.baseUtility

import android.content.Context
import android.widget.Toast

fun getMilliSecToTime(duration:Long):String{
    var currMilli= duration
    val hour:Long = currMilli/(3600*1000)
    currMilli %= (3600 * 1000)
    val minute:Long = currMilli/(60*1000)
    currMilli %= (60 * 1000)
    val sec:Long = currMilli/1000

    var result = ""
    if(hour>0){
        result+="${hour}:"
    }
    if(minute>0){
        if(minute<10 && hour>0){
            result+="0${minute}"
        }else{
            result+="${minute}"
        }
    }else{
        result+="00"
    }
    if(sec in 1..9){
        result+=":0${sec}"
    }else if(sec>=10){
        result+=":${sec}"
    }else{
        result+=":00"
    }
    return result
}

fun showToast(context:Context,msg:String,duration:Int){
    Toast.makeText(context,msg,duration).show()
}