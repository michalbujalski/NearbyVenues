package com.mb.nearbyvenues

import android.Manifest
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.annotation.IntegerRes
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.mb.domain.interactors.VenuesListUseCase
import com.mb.domain.interactors.VenuesUpdateUseCase
import com.mb.domain.models.Venue
import com.mb.nearbyvenues.presentation.VenueListPresenterImpl
import com.mb.nearbyvenues.presentation.VenuesListPresenter
//import com.mb.domain.models.Venue
import com.mb.nearbyvenues.presentation.VenuesListView
import com.mb.nearbyvenues.venues.HeaderState
import com.mb.nearbyvenues.venues.VenuesHeaderItem
import com.mb.nearbyvenues.venues.VenuesItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), VenuesListView {
    val venuesAdapter:ItemAdapter<VenuesItem> = ItemAdapter()
    val venuesHeaderAdapter:ItemAdapter<VenuesHeaderItem> = ItemAdapter()
    val fastAdapter:FastAdapter<*> by lazy{
        FastAdapter.with<IItem<*, *>, IAdapter<out IItem<*, *>>>(
                listOf(venuesHeaderAdapter, venuesAdapter)
        )
    }
    override fun requestPermission() {
        RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { isGranted ->
                    if(isGranted){
                        presenter.updateList()
                    }
                }
    }

    @Inject lateinit var venuesListUseCase: VenuesListUseCase
    @Inject lateinit var venuesUpdateUseCase: VenuesUpdateUseCase
    private lateinit var presenter: VenuesListPresenter

    override fun updateFinished() {
        venuesHeaderAdapter.set(listOf(VenuesHeaderItem(HeaderState.HIDE)))
    }

    override fun hideProgress() {
        venuesHeaderAdapter.set(listOf(VenuesHeaderItem(HeaderState.HIDE)))
    }

    override fun onNoConnection(retry: () -> Unit) {
        Snackbar.make(recyclerView,R.string.errorNoConnection,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.errorNoConnectionRetry,{retry()})
                .show()
    }

    override fun onNoConnection() {
        Snackbar.make(recyclerView,R.string.errorNoConnection,Snackbar.LENGTH_INDEFINITE).show()
    }

    override fun onErrorResponse(errMsg: String) {
        Snackbar.make(recyclerView,errMsg,Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok,{})
                .show()
    }

    override fun onErrorResponse(errorResponse:Int) {
        Snackbar.make(recyclerView,errorResponse,Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok,{})
                .show()
    }

    override fun setList(list: List<Venue>) {
        venuesAdapter.set(list.map { VenuesItem(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fastAdapter
        presenter = VenueListPresenterImpl(
                venuesListUseCase,
                venuesUpdateUseCase,
                this
        )
    }

    override fun onStart() {
        super.onStart()
        presenter.fetchList()
        presenter.updateList()
    }

    override fun onStop() {
        presenter.finish()
        super.onStop()
    }

    override fun isFetchingLocation() {
        venuesHeaderAdapter.set(listOf(VenuesHeaderItem(HeaderState.IS_FETCHING_LOCATION)))
    }

    override fun isFetchingVenues() {
        venuesHeaderAdapter.set(listOf(VenuesHeaderItem(HeaderState.IS_FETCHING_VENUES)))
    }

    override fun isFetchingVenuesDetails() {
        venuesHeaderAdapter.set(listOf(VenuesHeaderItem(HeaderState.IS_FETCHING_VENUES_DETAILS)))
    }

    override fun updateStarted() {
        venuesHeaderAdapter.set(listOf(VenuesHeaderItem(HeaderState.SHOW)))
    }
}
