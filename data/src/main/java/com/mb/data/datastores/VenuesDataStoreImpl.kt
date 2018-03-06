package com.mb.data.datastores

import com.mb.data.entities.VenueEntity
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class VenuesDataStoreImpl : VenuesDataStore{
    private val venues:ArrayList<VenueEntity> = ArrayList()
    private val subj:BehaviorSubject<List<VenueEntity>> = BehaviorSubject.create()

    override fun save(t: VenueEntity) {
        val idx = venues.indexOf(t)
        if(idx!=-1){
            venues[idx] = t
        }else{
            venues.add(t)
        }
        subj.onNext(venues)
    }

    override fun clear() {
        venues.clear()
        subj.onNext(venues)
    }

    override fun with(id: String): VenueEntity?{
        return venues.find { it.id == id }
    }

    override fun all(): List<VenueEntity> {
        return venues
    }

    override fun observeAll(): Observable<List<VenueEntity>>{
        return subj
    }
}