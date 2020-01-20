package com.example.footballapps.presentation.searchteam

import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.TheSportDBApi
import com.example.footballapps.datagson.model.TeamResponse
import com.example.footballapps.utils.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchTeamPresenter(
    val view: SearchTeamViews,
    val gson: Gson,
    private val apiRepository: ApiRepository,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getTeams(query: String) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(
                        TheSportDBApi.getTeams(query)
                    ).await(),
                TeamResponse::class.java
            ) ?: TeamResponse(listOf())

            view.showTeams(data.teams ?: listOf())
        }
    }
}