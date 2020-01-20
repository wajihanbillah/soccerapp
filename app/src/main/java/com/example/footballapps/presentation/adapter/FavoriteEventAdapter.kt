package com.example.footballapps.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapps.R
import com.example.footballapps.db.event.FavoriteEvent
import com.example.footballapps.utils.circularProgressBar
import kotlinx.android.synthetic.main.item_event.view.*

class FavoriteEventAdapter(
    val listener: OnFavoriteEventListener? = null,
    val data: MutableList<FavoriteEvent> = mutableListOf()
) :
    RecyclerView.Adapter<FavoriteEventAdapter.ViewHolder>() {

    fun setEventData(favoriteList: List<FavoriteEvent>) {
        if (data.size > 0) {
            data.clear()
        }
        data.addAll(favoriteList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_event,
            viewGroup, false
        )
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteItem: FavoriteEvent = data[position]
        val favoriteViewHolder = holder as FavoriteViewHolder
        favoriteViewHolder.bindFavoriteItem(favoriteItem)
    }

    open inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class FavoriteViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindFavoriteItem(favoriteItem: FavoriteEvent) {
            with(itemView) {
                tvEventName.text = favoriteItem.eventName
                tvEventDateTime.text = favoriteItem.eventDatetime
                tvTeamHome.text = favoriteItem.eventNameHome
                tvTeamAway.text = favoriteItem.eventNameAway

                tvScore.text =
                    String.format(
                        context.getString(R.string.format_score),
                        favoriteItem.eventScoreHome,
                        favoriteItem.eventScoreAway
                    )

                Glide.with(context)
                    .load(favoriteItem.eventBadgeHome)
                    .placeholder(circularProgressBar(context))
                    .into(imgBadgeHome)

                Glide.with(context)
                    .load(favoriteItem.eventBadgeAway)
                    .placeholder(circularProgressBar(context))
                    .into(imgBadgeAway)

                setOnClickListener {
                    listener?.onFavoriteEventItemClicked(favoriteItem)
                }
            }
        }
    }

    interface OnFavoriteEventListener {
        fun onFavoriteEventItemClicked(favoriteData: FavoriteEvent)
    }
}