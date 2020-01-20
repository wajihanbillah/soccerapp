package com.example.footballapps.datagson

import com.example.footballapps.BuildConfig

object TheSportDBApi {

    fun getLeagueList(): String {
        return BuildConfig.BASE_URL + "search_all_leagues.php/?s=" + "soccer"
    }

    fun getLeagueDetail(id: String): String {
        return BuildConfig.BASE_URL + "lookupleague.php/?id=" + id
    }

    fun getPastEvent(id: String): String {
        return BuildConfig.BASE_URL + "eventspastleague.php/?id=" + id
    }

    fun getNextEvent(id: String): String {
        return BuildConfig.BASE_URL + "eventsnextleague.php/?id=" + id
    }

    fun getEvents(query: String): String {
        return BuildConfig.BASE_URL + "searchevents.php/?e=" + query
    }

    fun getEventDetail(id: String): String {
        return BuildConfig.BASE_URL + "lookupevent.php/?id=" + id
    }

    fun getTeamDetail(id: String): String {
        return BuildConfig.BASE_URL + "lookupteam.php/?id=" + id
    }

    fun getTableList(id: String): String {
        return BuildConfig.BASE_URL + "lookuptable.php/?l=" + id
    }

    fun getTeamList(id: String): String {
        return BuildConfig.BASE_URL + "lookup_all_teams.php?id=" + id
    }

    fun getTeams(query: String): String {
        return BuildConfig.BASE_URL + "searchteams.php?t=" + query
    }
}