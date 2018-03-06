package com.mb.data.datastores

import com.mb.data.entities.VenueEntity
import io.reactivex.Observable

interface VenuesDataStore : DataStore<VenueEntity>{
    fun observeAll(): Observable<List<VenueEntity>>
}