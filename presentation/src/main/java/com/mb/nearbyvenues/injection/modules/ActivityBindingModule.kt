package com.mb.nearbyvenues.injection.modules

import com.mb.nearbyvenues.MainActivity
import com.mb.nearbyvenues.injection.scopes.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}