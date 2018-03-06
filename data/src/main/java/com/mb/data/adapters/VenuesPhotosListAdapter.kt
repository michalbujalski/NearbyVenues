package com.mb.data.adapters

import com.mb.data.entities.VenuePhotoEntity

class VenusePhotosResponseJson(override val response:VenuesPhotosJson):ResponseAdapter<VenuesPhotosJson>(response)
class VenuesPhotosJson(
        val photos:VenuesPhotosItems
)
class VenuesPhotosItems(
    val items:List<VenuePhotoEntity>
)