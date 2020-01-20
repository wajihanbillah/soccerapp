package com.example.footballapps.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapps.R
import com.example.footballapps.model.EventDetail
import kotlinx.android.synthetic.main.item_event_detail.view.*

class EventDetailAdapter(
    val data: MutableList<EventDetail> = mutableListOf()
) :
    RecyclerView.Adapter<EventDetailAdapter.ViewHolder>() {

    fun setEventDetailData(eventDetailList: List<EventDetail>) {
        if (data.size > 0) {
            data.clear()
        }
        data.addAll(eventDetailList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_event_detail,
            viewGroup, false
        )
        return EventDetailViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventDetailItem: EventDetail = data[position]
        val eventViewHolder = holder as EventDetailViewHolder
        eventViewHolder.bindEventDetailItem(eventDetailItem)
    }

    open inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class EventDetailViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindEventDetailItem(eventDetailItem: EventDetail) {
            with(itemView) {
                tvTeamHome.text =
                    if (eventDetailItem.home.isNotEmpty()) eventDetailItem.home else context.getString(
                        R.string.label_minus
                    )
                tvCategory.text =
                    if (eventDetailItem.category.isNotEmpty()) eventDetailItem.category else context.getString(
                        R.string.label_minus
                    )
                tvTeamAway.text =
                    if (eventDetailItem.away.isNotEmpty()) eventDetailItem.away else context.getString(
                        R.string.label_minus
                    )
            }
        }
    }
}