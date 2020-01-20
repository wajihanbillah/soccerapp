package com.example.footballapps.presentation.searchevent

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapps.R
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.model.Event
import com.example.footballapps.model.Team
import com.example.footballapps.presentation.adapter.EventAdapter
import com.example.footballapps.presentation.eventdetail.EventDetailActivity
import com.example.footballapps.utils.BundleKeys
import com.example.footballapps.utils.EspressoIdlingResource
import com.example.footballapps.utils.emptyString
import com.example.footballapps.utils.enum.TeamType
import com.example.footballapps.utils.setupToolbar
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.activity_search_event.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.startActivity

class SearchEventActivity : AppCompatActivity(), EventAdapter.OnEventListener, SearchEventViews {

    companion object {
        fun start(context: Context, idLeague: String) {
            context.startActivity<SearchEventActivity>(
                BundleKeys.LEAGUE_ID to idLeague
            )
        }
    }

    private var idLeague = emptyString()

    private lateinit var eventsAdapter: EventAdapter

    private lateinit var searchEventPresenter: SearchEventPresenter

    private var listEvent = mutableListOf<Event>()

    private var filteredEvent = listOf<Event>()

    private var listHomeId = mutableListOf<String>()

    private var listAwayId = mutableListOf<String>()

    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_event)

        idLeague = intent.getStringExtra(BundleKeys.LEAGUE_ID) ?: emptyString()

        setupToolbar(toolbar, getString(R.string.label_search_match), true)

        val request = ApiRepository()
        val gson = Gson()
        searchEventPresenter = SearchEventPresenter(this, gson, request)

        eventsAdapter = EventAdapter(listener = this)

        rvSearchEvent.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter = eventsAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search_view, menu)

        val searchItem = menu.findItem(R.id.menuSearchView)
        val searchView = searchItem.actionView as SearchView

        searchQuery(searchView)

        return true
    }

    private fun searchQuery(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    return false
                }

                EspressoIdlingResource.increment()
                searchEventPresenter.getSearchEvent(query.replace(" ", "+"))

                return true
            }
        }
        )
    }

    override fun onEventItemClicked(eventData: Event) {
        EventDetailActivity.start(this, eventData.idEvent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


    override fun showLoading() {
        msvSearchEvent.viewState = MultiStateView.ViewState.LOADING
    }

    override fun showSearchEvent(data: List<Event>) {
        listEvent.clear()
        listHomeId.clear()
        listAwayId.clear()

        listEvent = data.toMutableList()

        filteredEvent = listEvent.filter {
            it.strSport == getString(R.string.label_soccer_type) && it.idLeague == idLeague
        }

        eventsAdapter.setEventData(filteredEvent)

        filteredEvent.forEachIndexed { _, event ->
            listHomeId.add(event.idHomeTeam)
            listAwayId.add(event.idAwayTeam)
        }

        position = 0

        if (filteredEvent.isNotEmpty()) {
            msvSearchEvent.viewState = MultiStateView.ViewState.CONTENT

            if (!EspressoIdlingResource.idlingresource.isIdleNow) {
                //Memberitahukan bahwa tugas sudah selesai dijalankan
                EspressoIdlingResource.decrement()
            }

            searchEventPresenter.getTeamDetail(listHomeId[position], TeamType.HOME.type)
        } else {
            msvSearchEvent.viewState = MultiStateView.ViewState.EMPTY

            if (!EspressoIdlingResource.idlingresource.isIdleNow) {
                //Memberitahukan bahwa tugas sudah selesai dijalankan
                EspressoIdlingResource.decrement()
            }
        }
    }

    override fun showTeamDetail(data: List<Team>, type: String) {
        if (type == TeamType.HOME.type) {
            filteredEvent[position].strTeamBadgeHome = data[0].strTeamBadge

            eventsAdapter.addOrUpdate(filteredEvent[position])

            searchEventPresenter.getTeamDetail(listAwayId[position], TeamType.AWAY.type)
        } else {
            filteredEvent[position].strTeamBadgeAway = data[0].strTeamBadge

            eventsAdapter.addOrUpdate(filteredEvent[position])

            if (position < filteredEvent.size - 1) {

                position++

                searchEventPresenter.getTeamDetail(listHomeId[position], TeamType.HOME.type)
            } else {
                position = 0
            }
        }
    }
}
