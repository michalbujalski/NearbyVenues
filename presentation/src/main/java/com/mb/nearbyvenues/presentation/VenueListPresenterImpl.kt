package com.mb.nearbyvenues.presentation

import com.mb.data.providers.LocationProviderImpl
import com.mb.data.utils.addTo
import com.mb.data.utils.onIoThread
import com.mb.domain.interactors.VenuesListUseCase
import com.mb.domain.interactors.VenuesUpdateUseCase
import io.reactivex.disposables.CompositeDisposable

class VenueListPresenterImpl(
        private val venuesListUseCase: VenuesListUseCase,
        private val venuesUpdateUseCase: VenuesUpdateUseCase,
        private val view: VenuesListView
    ) : VenuesListPresenter {
    private val compositeDisposable = CompositeDisposable()

    override fun finish() {
        compositeDisposable.clear()
    }

    override fun updateList() {
        view.updateStarted()
        venuesUpdateUseCase
                .run()
                .onIoThread()
                .subscribe (
                { result ->
                    when(result){
                        VenuesUpdateUseCase.UpdateResult.FETCHING_LOCATION->view.isFetchingLocation()
                        VenuesUpdateUseCase.UpdateResult.FETCHING_VENUES->view.isFetchingVenues()
                        VenuesUpdateUseCase.UpdateResult.FETCHING_VENUES_DETAILS->view.isFetchingVenuesDetails()
                    }
                },{ e ->
                    if( e is LocationProviderImpl.LocationPermissionException){
                        view.requestPermission()
                    }else{
                        e.printStackTrace()
                    }
                }, {
                    view.updateFinished()
                })
                .addTo(compositeDisposable)
    }

    override fun fetchList() {
        venuesListUseCase.run()
                .onIoThread()
                .subscribe { list-> view.setList(list) }
                .addTo(compositeDisposable)
    }
}