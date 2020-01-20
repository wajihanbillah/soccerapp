package com.example.footballapps.api

import com.example.footballapps.TestContextProvider
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.datagson.model.*
import com.example.footballapps.model.Event
import com.example.footballapps.model.League
import com.example.footballapps.model.Team
import com.example.footballapps.presentation.event.EventListPresenter
import com.example.footballapps.presentation.event.EventListViews
import com.example.footballapps.presentation.eventdetail.EventDetailPresenter
import com.example.footballapps.presentation.eventdetail.EventDetailViews
import com.example.footballapps.presentation.leaguedetail.LeagueDetailPresenter
import com.example.footballapps.presentation.leaguedetail.LeagueDetailViews
import com.example.footballapps.presentation.main.LeagueListPresenter
import com.example.footballapps.presentation.main.LeagueListViews
import com.example.footballapps.presentation.searchevent.SearchEventPresenter
import com.example.footballapps.presentation.searchevent.SearchEventViews
import com.example.footballapps.utils.enum.EventType
import com.example.footballapps.utils.enum.TeamType
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PresenterTest {

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    @Mock
    private lateinit var apiResponse: Deferred<String>

    @Mock
    private lateinit var leagueListViews: LeagueListViews

    @Mock
    private lateinit var leagueDetailViews: LeagueDetailViews

    @Mock
    private lateinit var eventListViews: EventListViews

    @Mock
    private lateinit var searchEventViews: SearchEventViews

    @Mock
    private lateinit var eventDetailViews: EventDetailViews

    private lateinit var leagueListPresenter: LeagueListPresenter
    private lateinit var leagueDetailPresenter: LeagueDetailPresenter
    private lateinit var eventListPresenter: EventListPresenter
    private lateinit var eventDetailPresenter: EventDetailPresenter
    private lateinit var searchEventPresenter: SearchEventPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        leagueListPresenter = LeagueListPresenter(
            leagueListViews, gson, apiRepository,
            TestContextProvider()
        )

        leagueDetailPresenter = LeagueDetailPresenter(
            leagueDetailViews, gson, apiRepository,
            TestContextProvider()
        )

        eventListPresenter = EventListPresenter(
            eventListViews, gson, apiRepository,
            TestContextProvider()
        )

        eventDetailPresenter = EventDetailPresenter(
            eventDetailViews, gson, apiRepository,
            TestContextProvider()
        )

        searchEventPresenter = SearchEventPresenter(
            searchEventViews, gson, apiRepository,
            TestContextProvider()
        )
    }

    @Test
    fun getLeagueList() {
        val leagues: MutableList<League> = mutableListOf()
        val response = ListLeagueResponse(leagues)

        runBlocking {
            Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    ListLeagueResponse::class.java
                )
            ).thenReturn(response)

            leagueListPresenter.getLeagueList()

            Mockito.verify(leagueListViews).showLoading()
            Mockito.verify(leagueListViews).showLeagueList(leagues)
            Mockito.verify(leagueListViews).hideLoading()
        }
    }

    @Test
    fun getLeagueDetail() {
        val leagues: MutableList<League> = mutableListOf()
        val response = LeagueDetailResponse(leagues)
        val leagueId = "4328"

        runBlocking {
            Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    LeagueDetailResponse::class.java
                )
            ).thenReturn(response)

            leagueDetailPresenter.getLeagueDetail(leagueId)

            Mockito.verify(leagueDetailViews).showLoading()
            Mockito.verify(leagueDetailViews).showLeagueDetail(leagues)
            Mockito.verify(leagueDetailViews).hideLoading()
        }
    }

    @Test
    fun getEventList() {
        val events: MutableList<Event> = mutableListOf()
        val response = EventResponse(events)
        val leagueId = "4406"
        val eventType = EventType.LAST_MATCH.type

        runBlocking {
            Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    EventResponse::class.java
                )
            ).thenReturn(response)

            eventListPresenter.getEventList(leagueId, eventType)

            Mockito.verify(eventListViews).showLoading()
            Mockito.verify(eventListViews).showEventList(events)
        }
    }

    @Test
    fun getTeamDetail() {
        val team: MutableList<Team> = mutableListOf()
        val response = TeamResponse(team)
        val teamId = "133602"
        val teamType = TeamType.HOME.type

        runBlocking {
            Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    TeamResponse::class.java
                )
            ).thenReturn(response)

            eventListPresenter.getTeamDetail(teamId, teamType)

            Mockito.verify(eventListViews).showTeamDetail(team, teamType)
        }
    }

    @Test
    fun getEventDetail() {
        val event: MutableList<Event> = mutableListOf()
        val response = EventResponse(event)
        val eventId = "441613"

        runBlocking {
            Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    EventResponse::class.java
                )
            ).thenReturn(response)

            eventDetailPresenter.getEventDetail(eventId)

            Mockito.verify(eventDetailViews).showLoading()
            Mockito.verify(eventDetailViews).showEventDetail(event)
        }
    }

    @Test
    fun getSearchEvent() {
        val events: MutableList<Event> = mutableListOf()
        val response = SearchEventResponse(events)
        val query = "Racing club"

        runBlocking {
            Mockito.`when`(apiRepository.doRequestAsync(ArgumentMatchers.anyString()))
                .thenReturn(apiResponse)

            Mockito.`when`(apiResponse.await()).thenReturn("")

            Mockito.`when`(
                gson.fromJson(
                    "",
                    SearchEventResponse::class.java
                )
            ).thenReturn(response)

            searchEventPresenter.getSearchEvent(query)

            Mockito.verify(searchEventViews).showLoading()
            Mockito.verify(searchEventViews).showSearchEvent(events)
        }
    }
}