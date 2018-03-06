package com.mb.data.adapters

import com.mb.data.entities.VenueEntity

data class VenuesResponseJson(override val response:VenuesJson): ResponseAdapter<VenuesJson>(response)
data class VenuesJson(
    val venues:List<VenueEntity>
)