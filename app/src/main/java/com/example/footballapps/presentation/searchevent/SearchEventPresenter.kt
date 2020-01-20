package com.example.footballapps.presentation.searchevent

import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.TheSportDBApi
import com.example.footballapps.datagson.model.SearchEventResponse
import com.example.footballapps.datagson.model.TeamResponse
import com.example.footballapps.utils.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchEventPresenter(
    val view: SearchEventViews,
    val gson: Gson,
    private val apiRepository: ApiRepository,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getSearchEvent(query: String) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(
                        TheSportDBApi.getEvents(query)
                    ).await(),
                SearchEventResponse::class.java
            ) ?: SearchEventResponse(listOf())

            view.showSearchEvent(data.event ?: listOf())
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
            )

            view.showTeamDetail(data.teams ?: listOf(), type)
        }
    }
}