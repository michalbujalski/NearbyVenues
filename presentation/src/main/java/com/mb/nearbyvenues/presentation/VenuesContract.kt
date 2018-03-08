package com.mb.nearbyvenues.presentation

import com.mb.domain.models.Venue

interface VenuesListView {
    fun setList(list:List<Venue>)
    fun updateFinished()
    fun updateStarted()
    fun requestPermission()
    fun isFetchingLocation()
    fun isFetchingVenues()
    fun isFetchingVenuesDetails()
}
interface VenuesListPresenter {
    fun fetchList()
    fun updateList()
    fun finish()
}