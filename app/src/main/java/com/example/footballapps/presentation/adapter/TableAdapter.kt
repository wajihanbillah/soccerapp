package com.example.footballapps.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapps.R
import com.example.footballapps.model.Table
import com.example.footballapps.utils.circularProgressBar
import kotlinx.android.synthetic.main.item_table.view.*

class TableAdapter(
    val context: Context,
    val data: MutableList<Table> = mutableListOf()
) :
    RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    fun setTableData(tableList: List<Table>) {
        if (data.size > 0) {
            data.clear()
        }
        data.addAll(tableList)
        notifyDataSetChanged()
    }

    fun addOrUpdate(item: Table) {
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
            R.layout.item_table,
            viewGroup, false
        )
        return TableViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tableItem: Table = data[position]
        val tableViewHolder = holder as TableViewHolder
        tableViewHolder.bindTableItem(tableItem)
    }

    open inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class TableViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bindTableItem(tableItem: Table) {
            with(itemView) {

                background = if (adapterPosition % 2 == 0) ContextCompat.getDrawable(
                    context,
                    R.color.colorWhite
                ) else ContextCompat.getDrawable(context, R.color.colorPinkishGrey)

                Glide.with(context)
                    .load(tableItem.strTeamBadge)
                    .placeholder(circularProgressBar(context))
                    .into(imgTeamBadge)

                tvTeam.text = tableItem.name
                tvMatchPlayed.text = tableItem.played
                tvWin.text = tableItem.win
                tvDraw.text = tableItem.draw
                tvLose.text = tableItem.loss
                tvTotal.text = tableItem.total
            }
        }
    }
}