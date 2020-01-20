package com.example.footballapps.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapps.R
import com.example.footballapps.model.Event
import com.example.footballapps.utils.circularProgressBar
import com.example.footballapps.utils.dateConverter
import com.example.footballapps.utils.emptyString
import kotlinx.android.synthetic.main.item_event.view.*

class EventAdapter(
    val listener: OnEventListener? = null,
    val data: MutableList<Event> = mutableListOf()
) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    fun setEventData(eventList: List<Event>) {
        if (data.size > 0) {
            data.clear()
        }
        data.addAll(eventList)
        notifyDataSetChanged()
    }

    fun addOrUpdate(item: Event) {
        val i: Int = data.indexOf(item)
        if (i >= 0) {
            data[i] = item
            notifyDataSetChanged()
        } else {
            data.add(item)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_event,
            viewGroup, false
        )
        return EventViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventItem: Event = data[position]
        val eventViewHolder = holder as EventViewHolder
        eventViewHolder.bindEventItem(eventItem)
    }

    open inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class EventViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindEventItem(eventItem: Event) {
            with(itemView) {
                tvEventName.text = eventItem.strEvent
                tvEventDateTime.text =
                    dateConverter(
                        context,
                        eventItem.dateEvent ?: emptyString(),
                        eventItem.strTime ?: emptyString()
                    )
                tvTeamHome.text = eventItem.strHomeTeam
                tvTeamAway.text = eventItem.strAwayTeam

                val homeScore =
                    if (!eventItem.intHomeScore.isNullOrEmpty()) eventItem.intHomeScore else context.getString(
                        R.string.label_minus
                    )

                val awayScore =
                    if (!eventItem.intAwayScore.isNullOrEmpty()) eventItem.intAwayScore else context.getString(
                        R.string.label_minus
                    )

                tvScore.text =
                    String.format(context.getString(R.string.format_score), homeScore, awayScore)

                Glide.with(context)
                    .load(eventItem.strTeamBadgeHome)
                    .placeholder(circularProgressBar(context))
                    .into(imgBadgeHome)

                Glide.with(context)
                    .load(eventItem.strTeamBadgeAway)
                    .placeholder(circularProgressBar(context))
                    .into(imgBadgeAway)

                setOnClickListener {
                    listener?.onEventItemClicked(eventItem)
                }
            }
        }
    }

    interface OnEventListener {
        fun onEventItemClicked(eventData: Event)
    }
}