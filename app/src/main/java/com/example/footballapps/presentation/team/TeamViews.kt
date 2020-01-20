package com.example.footballapps.presentation.team

import com.example.footballapps.model.Team

interface TeamViews {
    fun showLoading()
    fun showTeamList(data: List<Team>)
}