package com.example.footballapps.presentation.eventdetail

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.footballapps.R
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.db.event.FavoriteEvent
import com.example.footballapps.db.event.databaseEvent
import com.example.footballapps.model.Event
import com.example.footballapps.model.EventDetail
import com.example.footballapps.model.Team
import com.example.footballapps.presentation.adapter.EventDetailAdapter
import com.example.footballapps.utils.*
import com.example.footballapps.utils.enum.TeamType
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.activity_event_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity

class EventDetailActivity : AppCompatActivity(), EventDetailViews {

    private var eventId = emptyString()

    private lateinit var eventDetailAdapter: EventDetailAdapter

    private lateinit var eventDetailPresenter: EventDetailPresenter

    private var isFavorite: Boolean = false

    private var isFavorable = false

    private var menuItem: Menu? = null

    private var eventName = emptyString()
    private var eventDateTime = emptyString()
    private var eventNameHome = emptyString()
    private var eventNameAway = emptyString()
    private var eventScoreHome = emptyString()
    private var eventScoreAway = emptyString()
    private var eventBadgeHome = emptyString()
    private var eventBadgeAway = emptyString()

    companion object {
        fun start(context: Context, eventId: String) {
            context.startActivity<EventDetailActivity>(
                BundleKeys.EVENT_ID to eventId
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event_detail)

        eventId = intent.getStringExtra(BundleKeys.EVENT_ID) ?: emptyString()

        setupToolbar(toolbar, getString(R.string.label_event_detail), true)

        val request = ApiRepository()
        val gson = Gson()
        eventDetailPresenter = EventDetailPresenter(this, gson, request)

        favoriteState()

        eventDetailAdapter = EventDetailAdapter(mutableListOf())

        rvEventDetail.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter = eventDetailAdapter
        }

        EspressoIdlingResource.increment()
        eventDetailPresenter.getEventDetail(eventId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        menuItem = menu

        menu?.getItem(0)?.isVisible = isFavorable

        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()
            }
        }
        return true
    }

    override fun showEventDetail(data: List<Event>) {
        if (data.isEmpty()) {

            msvEventDetail.viewState = MultiStateView.ViewState.EMPTY

        } else {

            eventName = data[0].strEvent
            eventDateTime = dateConverter(
                this,
                data[0].dateEvent ?: emptyString(),
                data[0].strTime ?: emptyString()
            )
            eventNameHome = data[0].strHomeTeam
            eventNameAway = data[0].strAwayTeam
            eventScoreHome = if (!data[0].intHomeScore.isNullOrEmpty()) data[0].intHomeScore
                ?: emptyString() else getString(
                R.string.label_minus
            )
            eventScoreAway = if (!data[0].intAwayScore.isNullOrEmpty()) data[0].intAwayScore
                ?: emptyString() else getString(
                R.string.label_minus
            )

            tvEventName.text = eventName
            tvEventDateTime.text = eventDateTime

            tvTeamHome.text = eventNameHome
            tvTeamAway.text = eventNameAway

            tvScore.text =
                String.format(getString(R.string.format_score), eventScoreHome, eventScoreAway)


            val listOfHome = listOf(
                data[0].strHomeGoalDetails,
                data[0].strHomeRedCards,
                data[0].strHomeYellowCards,
                data[0].strHomeFormation
            )
            val listOfCategory = resources.getStringArray(R.array.list_of_event_detail_category)
            val listOfAway = listOf(
                data[0].strAwayGoalDetails,
                data[0].strAwayRedCards,
                data[0].strAwayYellowCards,
                data[0].strAwayFormation
            )

            val eventDetail = mutableListOf<EventDetail>()

            listOfHome.forEachIndexed { i, _ ->
                eventDetail.add(
                    EventDetail(
                        listOfHome[i] ?: emptyString(),
                        listOfCategory[i],
                        listOfAway[i] ?: emptyString()
                    )
                )
            }

            eventDetailAdapter.setEventDetailData(eventDetail)

            eventDetailPresenter.getTeamDetail(
                data[0].idHomeTeam, TeamType.HOME.type
            )

            eventDetailPresenter.getTeamDetail(
                data[0].idAwayTeam, TeamType.AWAY.type
            )
        }
    }

    private fun addToFavorite() {
        try {
            databaseEvent.use {
                insert(
                    FavoriteEvent.TABLE_FAVORITE_EVENT,
                    FavoriteEvent.EVENT_ID to eventId,
                    FavoriteEvent.EVENT_NAME to eventName,
                    FavoriteEvent.EVENT_DATETIME to eventDateTime,
                    FavoriteEvent.EVENT_NAME_HOME to eventNameHome,
                    FavoriteEvent.EVENT_NAME_AWAY to eventNameAway,
                    FavoriteEvent.EVENT_SCORE_HOME to eventScoreHome,
                    FavoriteEvent.EVENT_SCORE_AWAY to eventScoreAway,
                    FavoriteEvent.EVENT_BADGE_HOME to eventBadgeHome,
                    FavoriteEvent.EVENT_BADGE_AWAY to eventBadgeAway
                )
            }
            Snackbar.make(llEventDetail, "Added to favorite", Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException) {
            Snackbar.make(llEventDetail, e.localizedMessage ?: emptyString(), Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun removeFromFavorite() {
        try {
            databaseEvent.use {
                delete(
                    FavoriteEvent.TABLE_FAVORITE_EVENT, "(EVENT_ID = {id})",
                    "id" to eventId
                )
            }
            Snackbar.make(llEventDetail, "Removed to favorite", Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException) {
            Snackbar.make(llEventDetail, e.localizedMessage ?: emptyString(), Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
        else
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
    }

    private fun favoriteState() {
        databaseEvent.use {
            val result = select(FavoriteEvent.TABLE_FAVORITE_EVENT)
                .whereArgs(
                    "(EVENT_ID = {id})",
                    "id" to eventId
                )
            val favorite = result.parseList(classParser<FavoriteEvent>())
            if (favorite.isNotEmpty()) isFavorite = true
        }
    }


    override fun showLoading() {
        msvEventDetail.viewState = MultiStateView.ViewState.LOADING
    }

    override fun showTeamDetail(data: List<Team>, type: String) {
        if (type == TeamType.HOME.type) {
            eventBadgeHome = data[0].strTeamBadge

            Glide.with(this)
                .load(eventBadgeHome)
                .into(imgBadgeHome)
        } else {
            msvEventDetail.viewState = MultiStateView.ViewState.CONTENT

            eventBadgeAway = data[0].strTeamBadge

            Glide.with(this)
                .load(eventBadgeAway)
                .into(imgBadgeAway)

            isFavorable = true

            invalidateOptionsMenu()
        }
    }
}
