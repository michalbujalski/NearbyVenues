package com.mb.domain.interactors

import com.mb.data.VenuesRepository
import com.mb.data.entities.CurrentLocationEntity
import com.mb.data.providers.LocationProvider
import com.mb.domain.VenueMapper
import com.mb.domain.models.Venue
import io.reactivex.Observable
import java.util.Collections.emptyList
import javax.inject.Inject

class VenuesListUseCase @Inject constructor(
        private val venuesRepository: VenuesRepository,
        private val venuesMapper: VenueMapper
): ObservableUseCase<List<Venue>, Void>() {
    override fun run(params: Void?): Observable<List<Venue>> {
        return venuesRepository
                .venues()
                .map{
                    it.map { venuesMapper.mapTo(it) }
                }
    }
}