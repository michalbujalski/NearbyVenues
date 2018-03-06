package com.mb.data

import com.mb.data.datastores.VenuesDataStore
import com.mb.data.entities.VenueEntity
import io.reactivex.Observable

interface VenuesRepository {
    fun venues() : Observable<List<VenueEntity>>
    fun updateVenues(list: List<VenueEntity>)
}

class VenuesRepositoryImpl (private val  venuesDataStore: VenuesDataStore): VenuesRepository{

    override fun venues(): Observable<List<VenueEntity>> {
        return venuesDataStore.observeAll()
    }

    override fun updateVenues(list: List<VenueEntity>) {
        return list.forEach{
            venuesDataStore.save(it)
        }
    }
}