package com.example.footballapps.presentation.main

import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.TheSportDBApi
import com.example.footballapps.datagson.model.ListLeagueResponse
import com.example.footballapps.utils.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LeagueListPresenter(
    val view: LeagueListViews,
    val gson: Gson,
    private val apiRepository: ApiRepository,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getLeagueList() {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(TheSportDBApi.getLeagueList()).await(),
                ListLeagueResponse::class.java
            ) ?: ListLeagueResponse(listOf())

            view.hideLoading()
            view.showLeagueList(data.countrys)
        }
    }
}