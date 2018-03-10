package com.mb.domain

import com.mb.domain.interactors.VenuesListUseCase
import org.junit.Before
import org.mockito.Mock

class VenuesListUseCaseTests{
    lateinit var useCase
    @Mock val 
    @Before
    fun setUp(){
        useCase = VenuesListUseCase()
    }
}