package com.mb.domain.interactors

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

interface UseCase<T, in Params> {
    fun run(params: Params?=null, compositeDisposable: CompositeDisposable?=null): T
}

abstract class ObservableUseCase<R, in Params>: UseCase<Observable<R>, Params> {
}

interface CompletableUseCase<in Params>: UseCase<Completable, Params> {
}