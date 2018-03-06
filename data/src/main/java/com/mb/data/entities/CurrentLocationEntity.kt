package com.mb.data.entities

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class CurrentLocationEntity(val lat: Double, val lng: Double) {
    private val df = DecimalFormat("#.00")
    init {
        val s = DecimalFormatSymbols()
        s.decimalSeparator = '.'
        df.decimalFormatSymbols = s
    }
    val langLat: String get() = df.format(lat) + "," + df.format(lng)
}