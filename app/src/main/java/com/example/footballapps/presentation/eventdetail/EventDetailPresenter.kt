package com.example.footballapps.presentation.eventdetail

import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.TheSportDBApi
import com.example.footballapps.datagson.model.EventResponse
import com.example.footballapps.datagson.model.TeamResponse
import com.example.footballapps.utils.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventDetailPresenter(
    val view: EventDetailViews,
    val gson: Gson,
    private val apiRepository: ApiRepository,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getEventDetail(id: String) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(
                        TheSportDBApi.getEventDetail(id)
                    ).await(),
                EventResponse::class.java
            ) ?: EventResponse(listOf())

            view.showEventDetail(data.events ?: listOf())
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