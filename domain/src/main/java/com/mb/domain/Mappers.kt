package com.mb.domain

import com.mb.data.entities.CurrentLocationEntity
import com.mb.data.entities.VenueEntity
import com.mb.data.entities.VenuePhotoEntity
import com.mb.domain.models.Venue
import com.mb.domain.models.VenuePhoto

interface MapperTo<in T, out R>{
    fun mapTo(t: T):R
}

class VenuePhotoMapper: MapperTo<VenuePhotoEntity, VenuePhoto>{
    override fun mapTo(t: VenuePhotoEntity): VenuePhoto =
            VenuePhoto(t.imageUrl)
}

class VenueMapper: MapperTo<VenueEntity, Venue>{
    private val photoMapper = VenuePhotoMapper()
    override fun mapTo(t: VenueEntity): Venue
            = Venue(t.photos.orEmpty().map { photoMapper.mapTo(it) })
}
