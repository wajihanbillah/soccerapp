package com.example.footballapps.db.event

data class FavoriteEvent(
    val id: Long?,
    val eventId: String?,
    val eventName: String?,
    val eventDatetime: String?,
    val eventNameHome: String?,
    val eventNameAway: String?,
    val eventScoreHome: String?,
    val eventScoreAway: String?,
    val eventBadgeHome: String?,
    val eventBadgeAway: String?
) {

    companion object {
        const val TABLE_FAVORITE_EVENT: String = "TABLE_FAVORITE_EVENT"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val EVENT_NAME: String = "EVENT_NAME"
        const val EVENT_DATETIME: String = "EVENT_DATETIME"
        const val EVENT_NAME_HOME: String = "EVENT_NAME_HOME"
        const val EVENT_NAME_AWAY: String = "EVENT_NAME_AWAY"
        const val EVENT_SCORE_HOME: String = "EVENT_SCORE_HOME"
        const val EVENT_SCORE_AWAY: String = "EVENT_SCORE_AWAY"
        const val EVENT_BADGE_HOME: String = "EVENT_BADGE_HOME"
        const val EVENT_BADGE_AWAY: String = "EVENT_BADGE_AWAY"
    }
}