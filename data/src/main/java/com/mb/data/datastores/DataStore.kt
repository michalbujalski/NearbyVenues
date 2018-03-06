package com.mb.data.datastores

interface DataStore<T> {
    fun save(t: T)
    fun clear()
    fun with(id: String): T?
    fun all(): List<T>
}