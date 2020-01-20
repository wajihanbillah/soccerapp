package com.example.footballapps.presentation.table

import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.TheSportDBApi
import com.example.footballapps.datagson.model.TableResponse
import com.example.footballapps.datagson.model.TeamResponse
import com.example.footballapps.utils.CoroutineContextProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TablePresenter(
    val view: TableViews,
    val gson: Gson,
    private val apiRepository: ApiRepository,
    private val context: CoroutineContextProvider = CoroutineContextProvider()
) {

    fun getTableList(id: String) {
        view.showLoading()

        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(
                        TheSportDBApi.getTableList(id)
                    ).await(),
                TableResponse::class.java
            ) ?: TableResponse(listOf())

            view.showTableList(data.table ?: listOf())
        }
    }

    fun getTeamDetail(id: String) {
        GlobalScope.launch(context.main) {
            val data = gson.fromJson(
                apiRepository
                    .doRequestAsync(
                        TheSportDBApi.getTeamDetail(id)
                    ).await(),
                TeamResponse::class.java
            )

            view.showTeamDetail(data.teams ?: listOf())
        }
    }
}