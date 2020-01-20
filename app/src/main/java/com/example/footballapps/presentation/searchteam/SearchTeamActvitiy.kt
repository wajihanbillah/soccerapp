package com.example.footballapps.presentation.searchteam

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapps.R
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.model.Team
import com.example.footballapps.presentation.adapter.TeamAdapter
import com.example.footballapps.presentation.team.TeamFragment
import com.example.footballapps.utils.BundleKeys
import com.example.footballapps.utils.EspressoIdlingResource
import com.example.footballapps.utils.emptyString
import com.example.footballapps.utils.setupToolbar
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.activity_search_team.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.startActivity

class SearchTeamActvitiy : AppCompatActivity(), SearchTeamViews, TeamAdapter.OnTeamListListener {

    companion object {
        fun start(context: Context, idLeague: String) {
            context.startActivity<SearchTeamActvitiy>(
                BundleKeys.LEAGUE_ID to idLeague
            )
        }
    }

    private lateinit var teamAdapter: TeamAdapter

    private lateinit var searchTeamPresenter: SearchTeamPresenter

    private var idLeague = emptyString()

    private var filteredTeam = listOf<Team>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_team)

        idLeague = intent.getStringExtra(BundleKeys.LEAGUE_ID) ?: emptyString()

        setupToolbar(toolbar, getString(R.string.label_search_team), true)

        val request = ApiRepository()
        val gson = Gson()
        searchTeamPresenter = SearchTeamPresenter(this, gson, request)

        teamAdapter = TeamAdapter(listener = this)

        rvSearchTeam.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter = teamAdapter
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
                searchTeamPresenter.getTeams(query.replace(" ", "+"))

                return true
            }
        }
        )
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
        msvSearchTeam.viewState = MultiStateView.ViewState.LOADING
    }

    override fun showTeams(data: List<Team>) {
        filteredTeam = data.filter {
            it.idLeague == idLeague
        }

        if (filteredTeam.isEmpty()) {

            msvSearchTeam.viewState = MultiStateView.ViewState.EMPTY
        } else {

            msvSearchTeam.viewState = MultiStateView.ViewState.CONTENT

            teamAdapter.setTeamData(filteredTeam)
        }
    }

    override fun onTeamItemClicked(teamData: Team) {

    }
}
