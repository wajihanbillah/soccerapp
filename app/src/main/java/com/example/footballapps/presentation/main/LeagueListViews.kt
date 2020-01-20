package com.example.footballapps.presentation.main

import com.example.footballapps.model.League

interface LeagueListViews {
    fun showLoading()
    fun hideLoading()
    fun showLeagueList(data: List<League>)
}