package com.example.footballapps.presentation.searchevent

import com.example.footballapps.model.Event
import com.example.footballapps.model.Team

interface SearchEventViews {
    fun showLoading()
    fun showSearchEvent(data: List<Event>)
    fun showTeamDetail(data: List<Team>, type: String)
}