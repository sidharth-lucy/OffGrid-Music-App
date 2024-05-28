package com.example.offgrid.chillMusic.result

sealed class ProcessResult<out D,out E> {

    data class Success<D>(var data:D):ProcessResult<D,Nothing>()

    data class Failed<E>(var msg:E):ProcessResult<Nothing,E>()

    data object Loading:ProcessResult<Nothing,Nothing>()
}