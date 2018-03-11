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

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        useCase = VenuesUpdateUseCase(locationProvider,venuesRepository)
    }

    @Test
    fun `test sucessfull venues update`(){
        val testObserver:TestObserver<VenuesUpdateUseCase.UpdateResult> = TestObserver.create()
        val venueId = "id"
        whenever(locationProvider.observeLocation()).thenReturn(Observable.just(CurrentLocationEntity(1.0,1.0)))
        whenever(venuesRepository.fetchVenues(any()))
                .thenReturn(
                        Observable
                                .just(
                                    arrayListOf(
                                        VenueEntity(id= venueId, photos=null)
                                    )
                                )
                )
        whenever(venuesRepository.fetchVenueDetails(any()))
                .thenReturn(Observable
                                .just(VenueEntity("id", emptyList())))

        useCase.run().subscribe(testObserver)

        testObserver.assertNoErrors()
        verify(locationProvider).observeLocation()
        verify(venuesRepository).fetchVenues(any())
        verify(venuesRepository, times(1)).fetchVenueDetails(any())

        testObserver.assertComplete()

    }
}