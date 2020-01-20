package com.example.footballapps.presentation.table

import com.example.footballapps.model.Table
import com.example.footballapps.model.Team

interface TableViews {
    fun showLoading()
    fun showTableList(data: List<Table>)
    fun showTeamDetail(data: List<Team>)
}