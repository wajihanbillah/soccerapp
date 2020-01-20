package com.example.footballapps.presentation.searchteam

import com.example.footballapps.model.Team

interface SearchTeamViews {
    fun showLoading()
    fun showTeams(data: List<Team>)
}