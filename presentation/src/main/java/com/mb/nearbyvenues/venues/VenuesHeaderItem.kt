package com.mb.nearbyvenues.venues

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mb.nearbyvenues.R
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_venues_header.view.*

class VenuesHeaderItem(private val state:HeaderState): AbstractItem<VenuesHeaderItem, VenuesHeaderVH>() {
    override fun getType():Int = 1

    override fun getViewHolder(v: View): VenuesHeaderVH = VenuesHeaderVH(v)

    override fun getLayoutRes(): Int = R.layout.item_venues_header

    override fun bindView(holder: VenuesHeaderVH?, payloads: MutableList<Any>?) {
        super.bindView(holder, payloads)

        when(state){
            HeaderState.IS_FETCHING_LOCATION -> {
                holder?.title?.setText(R.string.venuesHeaderIsFetchingLocation)
            }
            HeaderState.IS_FETCHING_VENUES -> {
                holder?.title?.setText(R.string.venuesHeaderIsFetchingVenues)
            }
            HeaderState.IS_FETCHING_VENUES_DETAILS -> {
                holder?.title?.setText(R.string.venuesHeaderIsFetchingVenusesDetails)
            }
            HeaderState.HIDE -> {
                holder?.progress?.visibility = View.GONE
                holder?.title?.visibility = View.GONE
            }
            HeaderState.SHOW -> {
                holder?.progress?.visibility = View.VISIBLE
                holder?.title?.visibility = View.VISIBLE
            }
        }
    }
}

enum class HeaderState{
    IS_FETCHING_LOCATION, IS_FETCHING_VENUES, IS_FETCHING_VENUES_DETAILS, FINISHED, HIDE,
    SHOW
}

class VenuesHeaderVH(itemView:View): RecyclerView.ViewHolder(itemView) {
    val title:TextView by lazy {
        itemView.venuesHeaderTitle
    }
    val progress:View by lazy {
        itemView.venuesHeaderProgress
    }
}