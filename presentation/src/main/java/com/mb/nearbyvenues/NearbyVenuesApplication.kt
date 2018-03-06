package com.mb.nearbyvenues

import com.mb.nearbyvenues.injection.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class NearbyVenuesApplication: DaggerApplication(){
    override fun applicationInjector(): AndroidInjector<out NearbyVenuesApplication> {
        return DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
    }

}