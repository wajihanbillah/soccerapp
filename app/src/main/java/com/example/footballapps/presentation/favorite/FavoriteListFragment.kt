package com.example.footballapps.presentation.favorite


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapps.R
import com.example.footballapps.db.event.FavoriteEvent
import com.example.footballapps.db.event.databaseEvent
import com.example.footballapps.db.team.FavoriteTeam
import com.example.footballapps.db.team.databaseTeam
import com.example.footballapps.presentation.adapter.FavoriteEventAdapter
import com.example.footballapps.presentation.adapter.FavoriteTeamAdapter
import com.example.footballapps.presentation.eventdetail.EventDetailActivity
import com.example.footballapps.presentation.teamdetail.TeamDetailActivity
import com.example.footballapps.utils.BundleKeys
import com.example.footballapps.utils.FavoriteType
import com.example.footballapps.utils.emptyString
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.fragment_favorite_event.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class FavoriteListFragment : Fragment(), FavoriteEventAdapter.OnFavoriteEventListener,
    FavoriteTeamAdapter.OnFavoriteTeamListener {
    override fun onFavoriteTeamItemClicked(teamData: FavoriteTeam) {

        context?.let {
            TeamDetailActivity.start(it, teamData.teamId ?: emptyString())
        }

    }

    override fun onFavoriteEventItemClicked(favoriteData: FavoriteEvent) {

        context?.let {
            EventDetailActivity.start(it, favoriteData.eventId ?: emptyString())
        }
    }

    companion object {
        fun newInstance(
            favoriteType: String = FavoriteType.FAVORITE_EVENT.type
        ): FavoriteListFragment {
            val fragment = FavoriteListFragment()
            val bundle = Bundle()
            bundle.putString(BundleKeys.FAVORITE_TYPE, favoriteType)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var favoriteType = emptyString()

    private lateinit var favoriteEventAdapter: FavoriteEventAdapter

    private lateinit var favoriteTeamAdapter: FavoriteTeamAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            favoriteType = it.getString(BundleKeys.FAVORITE_TYPE) ?: emptyString()
        }

        favoriteEventAdapter = FavoriteEventAdapter(listener = this)
        favoriteTeamAdapter = FavoriteTeamAdapter(listener = this)

        rvFavorite.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter =
                if (favoriteType == FavoriteType.FAVORITE_EVENT.type) favoriteEventAdapter else favoriteTeamAdapter
        }
    }


    private fun showFavorite() {
        context?.let {
            if (favoriteType == FavoriteType.FAVORITE_EVENT.type) {
                it.databaseEvent.use {
                    val result = select(FavoriteEvent.TABLE_FAVORITE_EVENT)
                    val favorite = result.parseList(classParser<FavoriteEvent>())

                    if (favorite.isNotEmpty()) {
                        msvFavorite.viewState = MultiStateView.ViewState.CONTENT
                    } else {
                        msvFavorite.viewState = MultiStateView.ViewState.EMPTY
                    }

                    favoriteEventAdapter.setEventData(favorite)
                }
            } else {
                it.databaseTeam.use {
                    val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
                    val favorite = result.parseList(classParser<FavoriteTeam>())

                    if (favorite.isNotEmpty()) {
                        msvFavorite.viewState = MultiStateView.ViewState.CONTENT
                    } else {
                        msvFavorite.viewState = MultiStateView.ViewState.EMPTY
                    }

                    favoriteTeamAdapter.setTeamData(favorite)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }
}
