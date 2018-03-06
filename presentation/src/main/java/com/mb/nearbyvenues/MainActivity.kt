package com.mb.nearbyvenues

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.mb.domain.interactors.VenuesListUseCase
import com.mb.domain.interactors.VenuesUpdateUseCase
import com.mb.domain.models.Venue
import com.mb.nearbyvenues.presentation.VenueListPresenterImpl
import com.mb.nearbyvenues.presentation.VenuesListPresenter
//import com.mb.domain.models.Venue
import com.mb.nearbyvenues.presentation.VenuesListView
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), VenuesListView {
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
        Log.v("list", "update")
    }

    override fun setList(list: List<Venue>) {
        Log.v("list", list.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = VenueListPresenterImpl(
                venuesListUseCase,
                venuesUpdateUseCase,
                this
        )
        presenter.fetchList()
        presenter.updateList()
    }
}
