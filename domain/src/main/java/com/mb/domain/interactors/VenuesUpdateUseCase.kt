package com.mb.domain.interactors

import com.mb.data.VenuesRepository
import com.mb.data.entities.CurrentLocationEntity
import com.mb.data.providers.LocationProvider
import com.mb.data.utils.addTo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class VenuesUpdateUseCase @Inject constructor(
        private val locationProvider: LocationProvider,
        private val venuesRepository: VenuesRepository
):ObservableUseCase<VenuesUpdateUseCase.UpdateResult,Void>() {

    private var subject:PublishSubject<UpdateResult> = PublishSubject.create()

    override fun run(params: Void?,
                     compositeDisposable: CompositeDisposable?
    ): Observable<UpdateResult> {
        subject = PublishSubject.create()
        locationProvider
                .observeLocation()
                .observeOn(Schedulers.io())
                .doOnNext{
                    subject.onNext(UpdateResult(type= UpdateResult.Type.FETCHING_LOCATION))
                }
                .map { CurrentLocationEntity(it.lat,it.lng) }
                .doOnNext{
                    subject.onNext(UpdateResult(type = UpdateResult.Type.FETCHING_VENUES))
                }
                .flatMap {
                    venuesRepository.fetchVenues(it)
                }
                .doOnNext{
                    subject.onNext(UpdateResult(type = UpdateResult.Type.FETCHING_VENUES_DETAILS))
                }
                .flatMap {
                    Observable.fromIterable(it)
                }
                .flatMap {
                    venuesRepository.fetchVenueDetails(it)
                }
                .toList()
                .doOnSuccess{venuesRepository.updateVenues(it)}
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            subject.onComplete()
                        },
                        { e ->
                            when (e) {
                                is HttpException ->
                                    subject.onNext(UpdateResult(error = ApiErrorFormatter.format(e)))
                                is UnknownHostException ->
                                    subject.onNext(UpdateResult(error= ApiException(ApiException.NO_CONNECTION)))
                                else ->
                                    subject.onError(e)
                            }
                        }
                ).addTo(compositeDisposable)
        return subject
    }

    class UpdateResult constructor(val type:Type? = null, val error:Exception? = null){
        enum class Type
        {
            FETCHING_LOCATION, FETCHING_VENUES, FETCHING_VENUES_DETAILS
        }
    }
    class ApiException constructor(val type:Int): Exception() {
        companion object {
            const val NO_CONNECTION = 1
            const val TO_MANY_REQUESTS = 2
            const val UNKNOWN = 3
        }
    }
    class ApiErrorFormatter{
        companion object {
            fun format(httpException: HttpException):ApiException{
                when(httpException.code()){
                    429 ->
                        return ApiException(ApiException.TO_MANY_REQUESTS)
                }
                return ApiException(ApiException.UNKNOWN)
            }
        }
    }
}