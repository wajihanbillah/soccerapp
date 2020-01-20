package com.example.footballapps.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapps.R
import com.example.footballapps.db.team.FavoriteTeam
import com.example.footballapps.model.Team
import com.example.footballapps.utils.circularProgressBar
import kotlinx.android.synthetic.main.item_team.view.*

class FavoriteTeamAdapter(
    val data: MutableList<FavoriteTeam> = mutableListOf(),
    val listener: OnFavoriteTeamListener? = null
    ) :
    RecyclerView.Adapter<FavoriteTeamAdapter.ViewHolder>() {

    fun setTeamData(teamList: List<FavoriteTeam>) {
        if (data.size > 0) {
            data.clear()
        }
        data.addAll(teamList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_team,
            viewGroup, false
        )
        return TeamViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teamItem: FavoriteTeam = data[position]
        val teamViewHolder = holder as TeamViewHolder
        teamViewHolder.bindTeamItem(teamItem)
    }

    open inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class TeamViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindTeamItem(teamItem: FavoriteTeam) {
            with(itemView) {
                Glide.with(context)
                    .load(teamItem.teamBadge)
                    .placeholder(circularProgressBar(context))
                    .into(imgTeamBadge)

                tvTitle.text = teamItem.teamName
                tvDescription.text = teamItem.teamDescription

                setOnClickListener {
                    listener?.onFavoriteTeamItemClicked(teamItem)
                }
            }
        }
    }

    interface OnFavoriteTeamListener {
        fun onFavoriteTeamItemClicked(teamData: FavoriteTeam)
    }
}