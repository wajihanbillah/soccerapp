package com.example.footballapps.presentation.main

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.footballapps.R
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.model.League
import com.example.footballapps.presentation.adapter.LeagueAdapter
import com.example.footballapps.presentation.favorite.FavoriteActivity
import com.example.footballapps.presentation.leaguedetail.LeagueDetailActivity
import com.example.footballapps.utils.EspressoIdlingResource
import com.example.footballapps.utils.setupToolbar
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class MainActivity : AppCompatActivity(), LeagueAdapter.OnLeagueListListener
    , LeagueListViews {

    companion object {
        fun start(context: Context) {
            context.startActivity(
                context.intentFor<MainActivity>(
                ).clearTask().newTask()
            )
        }
    }

    private lateinit var viewModelGson: LeagueListPresenter

    private lateinit var leagueAdapter: LeagueAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupToolbar(toolbar, getString(R.string.label_soccer_league))

        val request = ApiRepository()
        val gson = Gson()
        viewModelGson = LeagueListPresenter(this, gson, request)

        leagueAdapter = LeagueAdapter(listener = this)

        rvLeagueList.apply {
            layoutManager =
                GridLayoutManager(
                    this@MainActivity,
                    2,
                    GridLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter = leagueAdapter
        }

        srLeague.setOnRefreshListener {
            EspressoIdlingResource.increment()
            viewModelGson.getLeagueList()
        }

        EspressoIdlingResource.increment()
        viewModelGson.getLeagueList()
    }

    override fun onLeagueItemClicked(leagueData: League) {
        LeagueDetailActivity.start(this@MainActivity, leagueData.idLeague)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menuFavorite -> {
                FavoriteActivity.start(this)
            }
        }
        return true
    }

    override fun showLoading() {
        msvLeagueList.viewState = MultiStateView.ViewState.LOADING
    }

    override fun hideLoading() {
        srLeague.isRefreshing = false
        msvLeagueList.viewState = MultiStateView.ViewState.CONTENT
    }

    override fun showLeagueList(data: List<League>) {
        if (data.isEmpty()) {
            msvLeagueList.viewState = MultiStateView.ViewState.EMPTY
        } else {
            leagueAdapter.setLeagueData(data)
        }
        if (!EspressoIdlingResource.idlingresource.isIdleNow) {
            //Memberitahukan bahwa tugas sudah selesai dijalankan
            EspressoIdlingResource.decrement()
        }
    }
}
