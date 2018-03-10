package com.mb.data.providers
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.mb.data.entities.CurrentLocationEntity
import io.reactivex.Observable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import kotlin.reflect.jvm.internal.impl.javax.inject.Inject

class LocationProviderImpl @Inject constructor(private val context: Context):LocationProvider {

     @SuppressLint("MissingPermission")
     override fun observeLocation(): Observable<CurrentLocationEntity> {
         return if (ActivityCompat
                         .checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                 PackageManager.PERMISSION_GRANTED &&
                 ActivityCompat
                         .checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
         {
             Observable.error(LocationPermissionException())
         } else {
             val rxLocation = ReactiveLocationProvider(context)

             rxLocation.lastKnownLocation
                     .map { location ->
                         CurrentLocationEntity(location.latitude, location.longitude)
                     }.take(1)
         }
     }
    class LocationPermissionException:Exception()
}