package com.example.footballapps.presentation.teamdetail

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.footballapps.R
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.db.team.FavoriteTeam
import com.example.footballapps.db.team.databaseTeam
import com.example.footballapps.model.Team
import com.example.footballapps.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.activity_team_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity

class TeamDetailActivity : AppCompatActivity(), TeamDetailViews {

    companion object {
        fun start(context: Context, teamId: String) {
            context.startActivity<TeamDetailActivity>(
                BundleKeys.TEAM_ID to teamId
            )
        }
    }

    private var teamId = emptyString()

    private lateinit var teamDetailPresenter: TeamDetailPresenter

    private lateinit var team: Team

    private var isFavorite: Boolean = false

    private var menuItem: Menu? = null

    private var isFavorable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        teamId = intent.getStringExtra(BundleKeys.TEAM_ID) ?: emptyString()

        setupToolbar(toolbar, getString(R.string.label_team_detail), true)

        favoriteState()

        initButton()

        val request = ApiRepository()
        val gson = Gson()
        teamDetailPresenter = TeamDetailPresenter(this, gson, request)

        teamDetailPresenter.getTeamDetail(teamId)
    }

    private fun favoriteState() {
        databaseTeam.use {
            val result = select(FavoriteTeam.TABLE_FAVORITE_TEAM)
                .whereArgs(
                    "(TEAM_ID = {id})",
                    "id" to teamId
                )
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (favorite.isNotEmpty()) isFavorite = true
        }
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

    private fun removeFromFavorite() {
        try {
            databaseTeam.use {
                delete(
                    FavoriteTeam.TABLE_FAVORITE_TEAM, "(TEAM_ID = {id})",
                    "id" to teamId
                )
            }
            Snackbar.make(llTeamDetail, "Removed to favorite", Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException) {
            Snackbar.make(llTeamDetail, e.localizedMessage ?: emptyString(), Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun addToFavorite() {
        try {
            databaseTeam.use {
                insert(
                    FavoriteTeam.TABLE_FAVORITE_TEAM,
                    FavoriteTeam.TEAM_ID to team.idTeam,
                    FavoriteTeam.TEAM_NAME to team.strTeam,
                    FavoriteTeam.TEAM_DESCRIPTION to team.strDescriptionEN,
                    FavoriteTeam.TEAM_BADGE to team.strTeamBadge
                )
            }
            Snackbar.make(llTeamDetail, "Added to favorite", Snackbar.LENGTH_SHORT).show()
        } catch (e: SQLiteConstraintException) {
            Snackbar.make(llTeamDetail, e.localizedMessage ?: emptyString(), Snackbar.LENGTH_SHORT)
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

    override fun showLoading() {
        msvTeamDetail.viewState = MultiStateView.ViewState.LOADING
    }

    override fun showTeamDetail(data: List<Team>) {
        if (data.isEmpty()) {

            msvTeamDetail.viewState = MultiStateView.ViewState.EMPTY
        } else {

            team = data[0]

            msvTeamDetail.viewState = MultiStateView.ViewState.CONTENT

            Glide.with(this)
                .load(data[0].strTeamBadge)
                .placeholder(circularProgressBar(this))
                .into(imgTeamBadge)

            tvTitle.text = data[0].strTeam

            tvDate.text = String.format("Since %s", data[0].intFormedYear)

            tvAlternateName.text =
                if (data[0].strAlternate.isNotEmpty()) data[0].strAlternate else getString(
                    R.string.label_minus
                )

            tvDescription.text = data[0].strDescriptionEN

            isFavorable = true

            invalidateOptionsMenu()
        }
    }

    private fun initButton() {
        btnWebsite.setOnClickListener {
            openWebPage(this, team.strWebsite)
        }

        btnFacebook.setOnClickListener {
            openWebPage(this, team.strFacebook)
        }

        btnTwitter.setOnClickListener {
            openWebPage(this, team.strTwitter)
        }

        btnInstagram.setOnClickListener {
            openWebPage(this, team.strInstagram)
        }
    }
}
