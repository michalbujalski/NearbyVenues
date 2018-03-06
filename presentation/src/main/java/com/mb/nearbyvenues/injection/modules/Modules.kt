package com.mb.nearbyvenues.injection.modules

import android.content.Context
import com.mb.data.VenuesRepository
import com.mb.data.VenuesRepositoryImpl
import com.mb.data.datastores.VenuesDataStore
import com.mb.data.datastores.VenuesDataStoreImpl
import com.mb.data.network.NetworkServiceProvider
import com.mb.data.network.VenuesService
import com.mb.data.providers.LocationProvider
import com.mb.data.providers.LocationProviderImpl
import com.mb.domain.VenueMapper
import com.mb.domain.VenuePhotoMapper
import com.mb.domain.interactors.VenuesListUseCase
import com.mb.nearbyvenues.NearbyVenuesApplication
import com.mb.nearbyvenues.injection.scopes.PerActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Provides
    fun provideContext(app: NearbyVenuesApplication): Context{
        return app
    }
}

@Module
class UseCaseModule {
    @Provides
    @PerActivity
    fun venuesListUseCase(
            repository: VenuesRepository,
            venuesMapper: VenueMapper
    ):VenuesListUseCase{
        return VenuesListUseCase(repository, venuesMapper)
    }

    @Provides
    @PerActivity
    fun venuesUpdateUserCase(
            repository: VenuesRepository,
            venuesMapper: VenueMapper
    ):VenuesListUseCase{
        return VenuesListUseCase(repository, venuesMapper)
    }
}

@Module
class ProviderModule {
    @Provides
    @Singleton
    fun locationProvider(ctx: Context): LocationProvider = LocationProviderImpl(ctx)
}

@Module
class DataModule {

    @Provides
    @Singleton
    fun venueDataStore(): VenuesDataStore = VenuesDataStoreImpl()
    @Provides
    @Singleton
    fun venuesRepository(venuesDataStore: VenuesDataStore): VenuesRepository
            = VenuesRepositoryImpl(venuesDataStore)
}

@Module
class MappersModule {
    @Provides
    @Singleton
    fun venuesMapper()=VenueMapper()
    @Provides
    @Singleton
    fun venuesPhotoMapper()=VenuePhotoMapper()
}

@Module
class NetworkModule{
    @Provides
    @Singleton
    fun venuseService():VenuesService{
        return NetworkServiceProvider().venuesService()
    }
}