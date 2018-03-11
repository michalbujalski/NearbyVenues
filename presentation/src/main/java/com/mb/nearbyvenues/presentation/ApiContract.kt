package com.mb.nearbyvenues.presentation

interface ApiContract {
    fun onNoConnection()
    fun onNoConnection(retry: ()-> Unit)
    fun onErrorResponse(errMsg: Int)
    fun onErrorResponse(errMsg: String)
}