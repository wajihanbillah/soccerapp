package com.example.footballapps.model

import com.example.footballapps.utils.emptyString

data class Table (
    val name: String,
    val teamid: String,
    val played: String,
    val win: String,
    val draw: String,
    val loss: String,
    val total: String,
    var strTeamBadge: String = emptyString()
)