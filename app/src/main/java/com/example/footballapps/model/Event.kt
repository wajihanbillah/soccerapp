package com.example.footballapps.model

import com.example.footballapps.utils.emptyString

data class Event(
    val idEvent: String,
    val idLeague: String,
    val strEvent: String,
    val strHomeTeam: String,
    val strAwayTeam: String,
    val intHomeScore: String?,
    val intAwayScore: String?,
    val dateEvent: String?,
    val strTime: String?,
    val idHomeTeam: String,
    val idAwayTeam: String,
    val strSport: String = emptyString(),
    val strHomeGoalDetails: String?,
    val strHomeRedCards: String?,
    val strHomeYellowCards: String?,
    val strHomeFormation: String?,
    val strAwayGoalDetails: String?,
    val strAwayRedCards: String?,
    val strAwayYellowCards: String?,
    val strAwayFormation: String?,
    var strTeamBadgeHome: String = emptyString(),
    var strTeamBadgeAway: String = emptyString()
)