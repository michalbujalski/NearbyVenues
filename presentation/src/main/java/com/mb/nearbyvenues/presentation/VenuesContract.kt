package com.mb.nearbyvenues.presentation

import com.mb.domain.models.Venue

interface VenuesListView {
    fun setList(list:List<Venue>)
    fun updateFinished()
    fun requestPermission()
}
interface VenuesListPresenter {
    fun fetchList()
    fun updateList()
}