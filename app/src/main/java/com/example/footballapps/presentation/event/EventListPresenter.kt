package com.example.footballapps.presentation.event

import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.TheSportDBApi
import com.example.footballapps.datagson.model.EventResponse
import com.example.footballapps.datagson.model.TeamResponse
import com.example.footballapps.utils.CoroutineContextProvider
import com.example.footballapps.utils.enum.EventType
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventListPresenter(
    val view: EventListViews,
    val gson: Gson,
    private val apiRepository: ApiRepository,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getEventList(id: String, type: String) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(
                        if (type == EventType.LAST_MATCH.type)
                            TheSportDBApi.getPastEvent(id)
                        else
                            TheSportDBApi.getNextEvent(id)
                    ).await(),
                EventResponse::class.java
            ) ?: EventResponse(listOf())

            view.showEventList(data.events ?: listOf())
        }
    }

    fun getTeamDetail(id: String, type: String) {
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(
                        TheSportDBApi.getTeamDetail(id)
                    ).await(),
                TeamResponse::class.java
            ) ?: TeamResponse(listOf())

            view.showTeamDetail(data.teams ?: listOf(), type)
        }
    }
}