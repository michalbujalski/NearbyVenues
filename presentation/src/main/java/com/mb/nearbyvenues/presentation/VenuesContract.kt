package com.mb.nearbyvenues.presentation

import android.support.annotation.IntegerRes
import com.mb.domain.models.Venue

interface VenuesListView:ApiContract {
    fun setList(list:List<Venue>)
    fun updateFinished()
    fun updateStarted()
    fun requestPermission()
    fun isFetchingLocation()
    fun isFetchingVenues()
    fun isFetchingVenuesDetails()
    fun hideProgress()
}
interface VenuesListPresenter {
    fun fetchList()
    fun updateList()
    fun finish()
}