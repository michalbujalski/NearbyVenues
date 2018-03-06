package com.mb.nearbyvenues.presentation

import com.mb.data.providers.LocationProviderImpl
import com.mb.domain.interactors.VenuesListUseCase
import com.mb.domain.interactors.VenuesUpdateUseCase
import com.mb.nearbyvenues.utils.onIoThread

class VenueListPresenterImpl(
        private val venuesListUseCase: VenuesListUseCase,
        private val venuesUpdateUseCase: VenuesUpdateUseCase,
        private val view: VenuesListView
    ) : VenuesListPresenter {
    override fun updateList() {
        venuesUpdateUseCase
                .run()
                .onIoThread()
                .subscribe ({
                    view.updateFinished()
                },{ e ->
                    if( e is LocationProviderImpl.LocationPermissionException){
                        view.requestPermission()
                    }else{
                        e.printStackTrace()
                    }
                })
    }

    override fun fetchList() {
        venuesListUseCase.run()
                .onIoThread()
                .subscribe { list-> view.setList(list) }
    }
}