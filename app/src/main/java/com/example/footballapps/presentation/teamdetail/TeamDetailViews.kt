package com.example.footballapps.presentation.teamdetail

import com.example.footballapps.model.Team

interface TeamDetailViews {
    fun showLoading()
    fun showTeamDetail(data: List<Team>)
}