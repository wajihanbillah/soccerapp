package com.example.footballapps.presentation.event

import com.example.footballapps.model.Event
import com.example.footballapps.model.Team

interface EventListViews {
    fun showLoading()
    fun showEventList(data: List<Event>)
    fun showTeamDetail(data: List<Team>, type: String)
}