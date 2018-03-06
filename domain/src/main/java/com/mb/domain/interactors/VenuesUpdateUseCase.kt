package com.mb.domain.interactors

import com.mb.data.VenuesRepository
import com.mb.data.entities.CurrentLocationEntity
import com.mb.data.entities.VenueEntity
import com.mb.data.network.VenuesService
import com.mb.data.providers.LocationProvider
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class VenuesUpdateUseCase @Inject constructor(
        private val locationProvider: LocationProvider,
        private val venuesRepository: VenuesRepository,
        private val venuesService: VenuesService
):CompletableUseCase<Void> {
    override fun run(params: Void?): Completable {
        return locationProvider.observeLocation()
                .map { CurrentLocationEntity(it.lat,it.lng) }
                .flatMap {
                    venuesService.fetchVenues(it.langLat)
                }
                .flatMap {
                    Observable.fromIterable(it.response.venues)
                }
                .map {
                    val id = it.id
                    val photos = id?.let {
                        venuesService.fetchVenuePhoto(it).blockingGet().response.photos.items
                    }.orEmpty()
                    VenueEntity(id=id,photos = photos)
                }
                .toList()
                .doOnSuccess{venuesRepository.updateVenues(it)}
                .toCompletable()

    }
}