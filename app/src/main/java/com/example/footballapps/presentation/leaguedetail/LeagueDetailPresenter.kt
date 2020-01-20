package com.example.footballapps.presentation.leaguedetail

import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.TheSportDBApi
import com.example.footballapps.datagson.model.LeagueDetailResponse
import com.example.footballapps.utils.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LeagueDetailPresenter(
    val view: LeagueDetailViews,
    val gson: Gson,
    private val apiRepository: ApiRepository,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getLeagueDetail(id: String) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(TheSportDBApi.getLeagueDetail(id)).await(),
                LeagueDetailResponse::class.java
            ) ?: LeagueDetailResponse(listOf())

            view.hideLoading()
            view.showLeagueDetail(data.leagues)
        }
    }
}