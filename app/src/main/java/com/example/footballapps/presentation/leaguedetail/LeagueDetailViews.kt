package com.example.footballapps.presentation.leaguedetail

import com.example.footballapps.model.League

interface LeagueDetailViews {
    fun showLoading()
    fun hideLoading()
    fun showLeagueDetail(data: List<League>)
}