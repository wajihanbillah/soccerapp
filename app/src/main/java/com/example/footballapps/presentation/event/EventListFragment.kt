package com.example.footballapps.presentation.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.example.footballapps.utils.enum.EventType
import com.example.footballapps.utils.enum.TeamType
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.fragment_event_list.*


class EventListFragment : Fragment(), EventAdapter.OnEventListener, EventListViews {

    private var eventType = emptyString()

    private lateinit var eventsAdapter: EventAdapter

    private var idLeague = emptyString()

    private lateinit var eventListPresenter: EventListPresenter

    private var listEvent = mutableListOf<Event>()

    private var listHomeId = mutableListOf<String>()

    private var listAwayId = mutableListOf<String>()

    private var position = 0

    companion object {
        fun newInstance(
            eventType: String = EventType.LAST_MATCH.type,
            idLeague: String
        ): EventListFragment {
            val fragment = EventListFragment()
            val bundle = Bundle()
            bundle.putString(BundleKeys.EVENT_TYPE, eventType)
            bundle.putString(BundleKeys.LEAGUE_ID, idLeague)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            eventType = it.getString(BundleKeys.EVENT_TYPE) ?: emptyString()
            idLeague = it.getString(BundleKeys.LEAGUE_ID) ?: emptyString()
        }

        val request = ApiRepository()
        val gson = Gson()
        eventListPresenter = EventListPresenter(this, gson, request)

        eventsAdapter = EventAdapter(listener = this)

        rvEvent.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter = eventsAdapter
        }

        eventListPresenter.getEventList(idLeague, eventType)
    }

    override fun onEventItemClicked(eventData: Event) {
        context?.let {
            EventDetailActivity.start(it, eventData.idEvent)
        }
    }

    override fun showLoading() {
        msvEvent.viewState = MultiStateView.ViewState.LOADING
    }

    override fun showEventList(data: List<Event>) {

        position = 0

        if (data.isEmpty()) {

            msvEvent.viewState = MultiStateView.ViewState.EMPTY

            if (!EspressoIdlingResource.idlingresource.isIdleNow) {
                //Memberitahukan bahwa tugas sudah selesai dijalankan
                EspressoIdlingResource.decrement()
            }
        } else {

            listEvent.clear()
            listHomeId.clear()
            listAwayId.clear()

            listEvent = data.toMutableList()

            eventsAdapter.setEventData(listEvent)


            listEvent.forEachIndexed { _, event ->
                listHomeId.add(event.idHomeTeam)
                listAwayId.add(event.idAwayTeam)
            }

            msvEvent.viewState = MultiStateView.ViewState.CONTENT

            if (!EspressoIdlingResource.idlingresource.isIdleNow) {
                //Memberitahukan bahwa tugas sudah selesai dijalankan
                EspressoIdlingResource.decrement()
            }

            eventListPresenter.getTeamDetail(listHomeId[position], TeamType.HOME.type)
        }
    }

    override fun showTeamDetail(data: List<Team>, type: String) {
        if (type == TeamType.HOME.type) {
            listEvent[position].strTeamBadgeHome = data[0].strTeamBadge

            eventsAdapter.addOrUpdate(listEvent[position])

            eventListPresenter.getTeamDetail(listAwayId[position], TeamType.AWAY.type)
        } else {
            listEvent[position].strTeamBadgeAway = data[0].strTeamBadge

            eventsAdapter.addOrUpdate(listEvent[position])

            if (position < listEvent.size - 1) {
                position++

                eventListPresenter.getTeamDetail(listHomeId[position], TeamType.HOME.type)
            }
        }
    }
}
