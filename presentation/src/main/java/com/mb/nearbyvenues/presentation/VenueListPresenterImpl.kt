package com.mb.nearbyvenues.presentation

import com.mb.data.providers.LocationProviderImpl
import com.mb.data.utils.addTo
import com.mb.data.utils.onIoThread
import com.mb.domain.interactors.VenuesListUseCase
import com.mb.domain.interactors.VenuesUpdateUseCase
import com.mb.nearbyvenues.R
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
                .run(compositeDisposable = compositeDisposable)
                .onIoThread()
                .subscribe (
                { result ->
                    result.type?.let {
                        when(it){
                            VenuesUpdateUseCase.UpdateResult.Type.FETCHING_LOCATION->view.isFetchingLocation()
                            VenuesUpdateUseCase.UpdateResult.Type.FETCHING_VENUES->view.isFetchingVenues()
                            VenuesUpdateUseCase.UpdateResult.Type.FETCHING_VENUES_DETAILS->view.isFetchingVenuesDetails()
                        }
                    }

                    result.error?.let {
                        handleError(it)
                    }
                },{ e ->
                    handleError(e)
                }, {
                    view.updateFinished()
                })
                .addTo(compositeDisposable)
    }

    private fun handleError(exception:Throwable){
        view.hideProgress()
        when (exception) {
            is LocationProviderImpl.LocationPermissionException -> view.requestPermission()
            is VenuesUpdateUseCase.ApiException -> {
                when(exception.type){
                    VenuesUpdateUseCase.ApiException.NO_CONNECTION ->
                        view.onNoConnection({updateList()})
                    VenuesUpdateUseCase.ApiException.TO_MANY_REQUESTS ->
                        view.onErrorResponse(R.string.errorToManyRequests)
                }
            }
            else -> {
                view.onErrorResponse(exception.message ?: "Unknown error")
                exception.printStackTrace()
            }
        }
    }

    override fun fetchList() {
        venuesListUseCase
                .run()
                .onIoThread()
                .subscribe { list-> view.setList(list) }
                .addTo(compositeDisposable)
    }
}