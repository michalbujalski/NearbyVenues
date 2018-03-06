package com.mb.data.entities

data class VenuePhotoEntity(
    val prefix:String,
    val suffix:String,
    val width:String,
    val height:String
){
    val imageUrl: String get() = prefix + width + "x" + height + suffix;
}