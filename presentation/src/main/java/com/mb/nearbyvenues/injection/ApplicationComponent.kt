package com.mb.nearbyvenues.injection

import com.mb.nearbyvenues.NearbyVenuesApplication
import com.mb.nearbyvenues.injection.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ActivityBindingModule::class,
    ProviderModule::class,
    NetworkModule::class,
    DataModule::class,
    MappersModule::class
])
@Singleton
interface ApplicationComponent: AndroidInjector<NearbyVenuesApplication> {
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: NearbyVenuesApplication): Builder
        fun build(): ApplicationComponent
    }
}