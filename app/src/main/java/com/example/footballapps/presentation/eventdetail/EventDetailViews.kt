package com.example.footballapps.presentation.eventdetail

import com.example.footballapps.model.Event
import com.example.footballapps.model.Team

interface EventDetailViews {
    fun showLoading()
    fun showEventDetail(data: List<Event>)
    fun showTeamDetail(data: List<Team>, type: String)
}