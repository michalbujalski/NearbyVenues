package com.mb.domain.interactors

import io.reactivex.Completable
import io.reactivex.Observable

interface UseCase<T, in Params> {
    fun run(params: Params?=null): T
}

interface ObservableUseCase<R, in Params>: UseCase<Observable<R>, Params> {
}

interface CompletableUseCase<in Params>: UseCase<Completable, Params> {
}