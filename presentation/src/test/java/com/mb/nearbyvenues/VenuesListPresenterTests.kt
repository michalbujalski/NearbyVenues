package com.mb.nearbyvenues

import com.mb.domain.interactors.VenuesListUseCase
import com.mb.domain.interactors.VenuesUpdateUseCase
import com.mb.domain.models.Venue
import com.mb.nearbyvenues.presentation.VenueListPresenterImpl
import com.mb.nearbyvenues.presentation.VenuesListView
import com.nhaarman.mockito_kotlin.*
import io.github.plastix.rxschedulerrule.RxSchedulerRule
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class VenuesListPresenterTests {

    @get:Rule
    val rule = RxSchedulerRule()

    protected lateinit var venuesPresenter: VenueListPresenterImpl
    @Mock
    lateinit var updateUseCase: VenuesUpdateUseCase
    @Mock
    lateinit var listUseCase: VenuesListUseCase
    @Mock
    lateinit var view: VenuesListView

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        venuesPresenter = VenueListPresenterImpl(
                listUseCase, updateUseCase, view
        )
    }

    @Test
    fun `test no actions on initial state`(){
        verifyZeroInteractions(view)
        verifyZeroInteractions(updateUseCase)
        verifyZeroInteractions(listUseCase)
    }

    @Test
    fun `observe venues list`() {
        val publishSubject: PublishSubject<List<Venue>> = PublishSubject.create()
        whenever(listUseCase.run()).thenReturn(publishSubject)

        venuesPresenter.fetchList()
        publishSubject.onNext(arrayListOf())
        publishSubject.onNext(arrayListOf())

        verify(view, times(2)).setList(anyList())
    }

    @Test
    fun `check showing update started info`() {
        val publishSubject: PublishSubject<VenuesUpdateUseCase.UpdateResult> = PublishSubject.create()
        whenever(updateUseCase.run()).thenReturn(publishSubject)

        venuesPresenter.updateList()
        publishSubject.onNext(VenuesUpdateUseCase.UpdateResult.FETCHING_VENUES)

        verify(view).updateStarted()
        verify(view).isFetchingVenues()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun `check showing location fetch started info`() {
        val publishSubject: PublishSubject<VenuesUpdateUseCase.UpdateResult> = PublishSubject.create()
        whenever(updateUseCase.run()).thenReturn(publishSubject)

        venuesPresenter.updateList()
        publishSubject.onNext(VenuesUpdateUseCase.UpdateResult.FETCHING_LOCATION)

        verify(view).isFetchingLocation()
    }

    @Test
    fun `check showing venues details fetch started info`() {
        val publishSubject: PublishSubject<VenuesUpdateUseCase.UpdateResult> = PublishSubject.create()
        whenever(updateUseCase.run()).thenReturn(publishSubject)

        venuesPresenter.updateList()
        publishSubject.onNext(VenuesUpdateUseCase.UpdateResult.FETCHING_VENUES_DETAILS)

        verify(view).isFetchingVenuesDetails()
    }

}