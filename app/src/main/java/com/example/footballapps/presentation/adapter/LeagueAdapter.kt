package com.example.footballapps.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapps.R
import com.example.footballapps.model.League
import com.example.footballapps.utils.circularProgressBar
import kotlinx.android.synthetic.main.item_league.view.*

class LeagueAdapter(
    val listener: OnLeagueListListener? = null,
    val data: MutableList<League> = mutableListOf()
) :
    RecyclerView.Adapter<LeagueAdapter.ViewHolder>() {

    fun setLeagueData(leagueList: List<League>) {
        if (data.size > 0) {
            data.clear()
        }
        data.addAll(leagueList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_league,
            viewGroup, false
        )
        return LeagueViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val leagueItem: League = data[position]
        val leagueViewHolder = holder as LeagueViewHolder
        leagueViewHolder.bindLeagueItem(leagueItem)
    }

    open inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class LeagueViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindLeagueItem(leagueItem: League) {
            with(itemView) {


                Glide.with(context)
                    .load(leagueItem.strBadge)
                    .placeholder(circularProgressBar(context))
                    .into(imgLeagueBadge)

                tvLeagueName.text = leagueItem.strLeague

                setOnClickListener {
                    listener?.onLeagueItemClicked(leagueItem)
                }
            }
        }
    }

    interface OnLeagueListListener {
        fun onLeagueItemClicked(leagueData: League)
    }
}