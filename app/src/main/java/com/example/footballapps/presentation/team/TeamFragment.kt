package com.example.footballapps.presentation.team


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapps.R
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.model.Team
import com.example.footballapps.presentation.adapter.TeamAdapter
import com.example.footballapps.presentation.teamdetail.TeamDetailActivity
import com.example.footballapps.utils.BundleKeys
import com.example.footballapps.utils.emptyString
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.fragment_team.*

class TeamFragment : Fragment(), TeamViews, TeamAdapter.OnTeamListListener {

    private var idLeague = emptyString()

    private lateinit var teamPresenter: TeamPresenter

    private lateinit var teamAdapter: TeamAdapter

    companion object {
        fun newInstance(
            idLeague: String
        ): TeamFragment {
            val fragment = TeamFragment()
            val bundle = Bundle()
            bundle.putString(BundleKeys.LEAGUE_ID, idLeague)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            idLeague = it.getString(BundleKeys.LEAGUE_ID) ?: emptyString()
        }

        val request = ApiRepository()
        val gson = Gson()
        teamPresenter = TeamPresenter(this, gson, request)

        context?.let {
            teamAdapter = TeamAdapter(listener = this)
        }

        rvTeam.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter = teamAdapter
        }

        teamPresenter.getTeamList(idLeague)
    }

    override fun showLoading() {
        msvTeam.viewState = MultiStateView.ViewState.LOADING
    }

    override fun showTeamList(data: List<Team>) {
        if (data.isEmpty()) {

            msvTeam.viewState = MultiStateView.ViewState.EMPTY
        } else {

            msvTeam.viewState = MultiStateView.ViewState.CONTENT

            teamAdapter.setTeamData(data)
        }
    }

    override fun onTeamItemClicked(teamData: Team) {
        context?.let {
            TeamDetailActivity.start(it, teamData.idTeam)
        }
    }
}
