package com.example.footballapps.presentation.teamdetail

import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.TheSportDBApi
import com.example.footballapps.datagson.model.TeamResponse
import com.example.footballapps.utils.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamDetailPresenter(
    val view: TeamDetailViews,
    val gson: Gson,
    private val apiRepository: ApiRepository,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getTeamDetail(id: String) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(
                        TheSportDBApi.getTeamDetail(id)
                    ).await(),
                TeamResponse::class.java
            ) ?: TeamResponse(listOf())

            view.showTeamDetail(data.teams ?: listOf())
        }
    }
}