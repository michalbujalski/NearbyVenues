package com.mb.domain

import com.mb.data.VenuesRepository
import com.mb.domain.interactors.VenuesListUseCase
import com.mb.domain.models.Venue
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class VenuesListUseCaseTests{
    lateinit var useCase:VenuesListUseCase
    @Mock lateinit var repository:VenuesRepository
    @Mock lateinit var venueMapper: VenueMapper

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        useCase = VenuesListUseCase(repository,venueMapper)
    }

    @Test
    fun `test fetching list`(){
        whenever(repository.venues()).thenReturn(Observable.just(arrayListOf()))
        val observer = useCase.run().test()
        observer.assertNoErrors()
        verify(repository).venues()
    }
}