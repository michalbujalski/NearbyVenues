package com.mb.data

import com.mb.data.datastores.VenuesDataStore
import com.mb.data.entities.CurrentLocationEntity
import com.mb.data.entities.VenueEntity
import com.mb.data.network.VenuesService
import io.reactivex.Observable
import io.reactivex.Single

interface VenuesRepository {
    fun venues() : Observable<List<VenueEntity>>
    fun updateVenues(list: List<VenueEntity>)
    fun fetchVenues(currentLocationEntity: CurrentLocationEntity):Observable<List<VenueEntity>>
    fun fetchVenueDetails(venueEntity: VenueEntity):Observable<VenueEntity>
}

class VenuesRepositoryImpl (private val  venuesDataStore: VenuesDataStore, private val venuesService: VenuesService): VenuesRepository{
    override fun fetchVenues(currentLocationEntity: CurrentLocationEntity): Observable<List<VenueEntity>> {
        return venuesService.fetchVenues(currentLocationEntity.langLat, 10).map { it.response.venues }
    }

    override fun fetchVenueDetails(venueEntity: VenueEntity): Observable<VenueEntity> {
        return venuesService
                .fetchVenuePhoto(venueEntity.id)
                .map { venueEntity.copy(photos = it.response.photos.items) }
    }

    override fun venues(): Observable<List<VenueEntity>> {
        return venuesDataStore.observeAll()
    }

    override fun updateVenues(list: List<VenueEntity>) {
        return list.forEach{
            venuesDataStore.save(it)
        }
    }
}