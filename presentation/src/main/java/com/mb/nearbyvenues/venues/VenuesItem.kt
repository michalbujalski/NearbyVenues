package com.mb.nearbyvenues.venues

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.mb.domain.models.Venue
import com.mb.nearbyvenues.GlideApp
import com.mb.nearbyvenues.R
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_venue.view.*

class VenuesItem(private val venue:Venue): AbstractItem<VenuesItem, VenuesItemVH>() {
    override fun getType() = R.id.venueItemPhoto

    override fun getViewHolder(v: View): VenuesItemVH = VenuesItemVH(v)

    override fun getLayoutRes(): Int = R.layout.item_venue

    override fun bindView(holder: VenuesItemVH?, payloads: MutableList<Any>?) {
        super.bindView(holder, payloads)
        holder?.loadPic(venue.photos.firstOrNull()?.url ?: "")
    }
}

class VenuesItemVH constructor(itemView: View):RecyclerView.ViewHolder(itemView){
    private val photo: ImageView by lazy{
        itemView.venueItemPhoto
    }
    private val context: Context by lazy{
        itemView.context
    }

    fun loadPic(url:String) {
        GlideApp.with(context).load(url).into(photo)
    }
}
