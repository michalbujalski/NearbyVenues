package com.mb.data.providers

import com.mb.data.entities.CurrentLocationEntity
import io.reactivex.Observable

interface LocationProvider {
    fun observeLocation(): Observable<CurrentLocationEntity>
}