package com.mb.domain

import com.mb.data.VenuesRepository
import com.mb.data.adapters.*
import com.mb.data.entities.CurrentLocationEntity
import com.mb.data.entities.VenueEntity
import com.mb.data.entities.VenuePhotoEntity
import com.mb.data.network.VenuesService
import com.mb.data.providers.LocationProvider
import com.mb.domain.interactors.VenuesUpdateUseCase
import com.nhaarman.mockito_kotlin.*
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class VenuesUpdateUseCaseTests {
    @get:Rule
    val rxSchedulers = RxSchedulerRule()
    lateinit var useCase:VenuesUpdateUseCase
    @Mock lateinit var locationProvider: LocationProvider
    @Mock lateinit var venuesRepository: VenuesRepository
    @Mock lateinit var venuesService: VenuesService

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        useCase = VenuesUpdateUseCase(locationProvider,venuesRepository,venuesService)
    }

    @Test
    fun `test sucessfull venues update`(){
        val testObserver:TestObserver<VenuesUpdateUseCase.UpdateResult> = TestObserver.create()
        val venueId = "id"
        whenever(locationProvider.observeLocation()).thenReturn(Observable.just(CurrentLocationEntity(1.0,1.0)))
        whenever(venuesService.fetchVenues(any(), any()))
                .thenReturn(
                        Observable
                                .just(
                                        VenuesResponseJson(
                                                VenuesJson(
                                                        arrayListOf(
                                                                VenueEntity(id= venueId, photos=null)
                                                        )
                                                )
                                        )
                                )
                )
        whenever(venuesService.fetchVenuePhoto(eq(venueId)))
                .thenReturn(Single
                                .just(
                                        VenusePhotosResponseJson(
                                                VenuesPhotosJson(
                                                        VenuesPhotosItems(
                                                                listOf(VenuePhotoEntity(
                                                                        "pref","suff",
                                                                        "100","100"))
                                                        )
                                                )
                                        )
                                ))

        useCase.run().subscribe(testObserver)

        testObserver.assertNoErrors()
        verify(locationProvider).observeLocation()
        verify(venuesService).fetchVenues(any(), any())
        verify(venuesService, times(1)).fetchVenuePhoto(any())

        testObserver.assertComplete()

    }
}