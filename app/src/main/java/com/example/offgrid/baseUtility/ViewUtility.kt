package com.example.offgrid.baseUtility

import android.view.View

fun View.visible(){
    this.visibility=View.VISIBLE
}

fun View.gone(){
    this.visibility=View.GONE
}

fun View.invisible(){
    this.visibility=View.INVISIBLE
}