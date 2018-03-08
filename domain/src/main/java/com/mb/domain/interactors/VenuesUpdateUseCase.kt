package com.mb.domain.interactors

import com.mb.data.VenuesRepository
import com.mb.data.entities.CurrentLocationEntity
import com.mb.data.entities.VenueEntity
import com.mb.data.network.VenuesService
import com.mb.data.providers.LocationProvider
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class VenuesUpdateUseCase @Inject constructor(
        private val locationProvider: LocationProvider,
        private val venuesRepository: VenuesRepository,
        private val venuesService: VenuesService
):ObservableUseCase<VenuesUpdateUseCase.UpdateResult,Void>() {
    private val behaviorSubject:BehaviorSubject<UpdateResult> = BehaviorSubject.create()

    override fun run(params: Void?): Observable<UpdateResult> {
        locationProvider
                .observeLocation()
                .observeOn(Schedulers.io())
                .doOnNext{
                    behaviorSubject.onNext(UpdateResult.FETCHING_LOCATION)
                }
                .map { CurrentLocationEntity(it.lat,it.lng) }
                .doOnNext{
                    behaviorSubject.onNext(UpdateResult.FETCHING_VENUES)
                }
                .flatMap {
                    venuesService.fetchVenues(it.langLat, 10)
                }
                .doOnNext{
                    behaviorSubject.onNext(UpdateResult.FETCHING_VENUES_DETAILS)
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
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            behaviorSubject.onComplete()
                        },
                        { e ->
                            behaviorSubject.onError(e)
                        }
                )
        return behaviorSubject
    }

    enum class UpdateResult{
        FETCHING_LOCATION, FETCHING_VENUES, FETCHING_VENUES_DETAILS, FETCH_ERROR
    }
}