package com.mb.data.network

import com.mb.data.adapters.VenuesResponseJson
import com.mb.data.adapters.VenusePhotosResponseJson
import com.mb.data.entities.VenuePhotoEntity
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VenuesService {
    @GET("venues/search")
    fun fetchVenues(@Query("ll") latLng: String,@Query("limit") limit:Int): Observable<VenuesResponseJson>

    @GET("venues/{venue_id}/photos")
    fun fetchVenuePhoto(@Path("venue_id") venueId: String): Single<VenusePhotosResponseJson>
}