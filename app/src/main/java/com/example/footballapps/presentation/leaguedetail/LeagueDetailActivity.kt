package com.example.footballapps.presentation.leaguedetail

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.footballapps.R
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.model.League
import com.example.footballapps.presentation.adapter.GeneralPagerAdapter
import com.example.footballapps.presentation.event.EventFragment
import com.example.footballapps.presentation.searchevent.SearchEventActivity
import com.example.footballapps.presentation.searchteam.SearchTeamActvitiy
import com.example.footballapps.presentation.table.TableFragment
import com.example.footballapps.presentation.team.TeamFragment
import com.example.footballapps.utils.*
import com.example.footballapps.utils.enum.SearchType
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.activity_league_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.startActivity


class LeagueDetailActivity : AppCompatActivity(), LeagueDetailViews {

    private lateinit var league: League

    private var menuItem: Menu? = null

    private var isSearchable = true

    private lateinit var leagueDetailPresenter: LeagueDetailPresenter

    private var idLeague = emptyString()

    companion object {
        fun start(context: Context, leagueId: String) {
            context.startActivity<LeagueDetailActivity>(
                BundleKeys.LEAGUE_ID to leagueId
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_league_detail)

        idLeague = intent.getStringExtra(BundleKeys.LEAGUE_ID) ?: emptyString()

        setupToolbar(
            toolbar,
            getString(R.string.label_league_detail),
            true
        )

        val request = ApiRepository()
        val gson = Gson()
        leagueDetailPresenter = LeagueDetailPresenter(this, gson, request)

        initViewPagerLeagueDetail()

        initButton()

        leagueDetailPresenter.getLeagueDetail(idLeague)
    }

    private fun initButton() {
        btnMore.setOnClickListener {
            if (btnMore.text == getString(R.string.action_more)) {
                btnMore.text = getString(R.string.action_hide)
                btnMore.icon =
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_keyboard_arrow_up_black
                    )

                tvDescription.maxLines = Int.MAX_VALUE
            } else {
                btnMore.text = getString(R.string.action_more)
                btnMore.icon =
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_keyboard_arrow_down_black
                    )

                tvDescription.maxLines = 5
            }
        }

        btnWebsite.setOnClickListener {
            openWebPage(this, league.strWebsite)
        }

        btnFacebook.setOnClickListener {
            openWebPage(this, league.strFacebook)
        }

        btnTwitter.setOnClickListener {
            openWebPage(this, league.strTwitter)
        }

        btnYoutube.setOnClickListener {
            openWebPage(this, league.strYoutube)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.menuSearch -> {
                if (vpLeagueDetail.currentItem == SearchType.SEARCH_EVENT.ordinal) {
                    SearchEventActivity.start(this, idLeague)
                } else {
                    SearchTeamActvitiy.start(this, idLeague)
                }
            }
        }
        return true
    }

    override fun showLeagueDetail(data: List<League>) {

//        isSearchable = true
//
//        invalidateOptionsMenu()

        if (data.isEmpty()) {
            msvLeagueDetail.viewState = MultiStateView.ViewState.EMPTY
        } else {
            league = data[0]

            tvTitle.text = data[0].strLeague
            tvCountry.text = String.format(
                getString(R.string.format_country),
                data[0].strCountry
            )
            tvYearFormed.text =
                String.format(
                    getString(R.string.format_year_formed),
                    data[0].intFormedYear
                )
            tvDescription.text = data[0].strDescriptionEN

            Glide.with(this)
                .load(data[0].strTrophy)
                .placeholder(circularProgressBar(this))
                .into(imgTrophy)
        }
    }

    override fun showLoading() {
        msvLeagueDetail.viewState = MultiStateView.ViewState.LOADING
    }

    override fun hideLoading() {
        msvLeagueDetail.viewState = MultiStateView.ViewState.CONTENT
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_league_detail, menu)
        menuItem = menu

        menu?.getItem(0)?.isVisible = isSearchable

        return true
    }

    private fun initViewPagerLeagueDetail() {
        vpLeagueDetail.adapter =
            GeneralPagerAdapter(
                supportFragmentManager,
                pages = leagueDetailPages(idLeague),
                title = leagueDetailPagesTitle(this)
            )
        vpLeagueDetail.offscreenPageLimit = 3
        tabLeagueDetail.setupWithViewPager(vpLeagueDetail)

        vpLeagueDetail.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

                isSearchable = position != 1

                invalidateOptionsMenu()
            }

        })
    }

    private fun leagueDetailPages(idLeague: String): List<Fragment> = listOf(
        EventFragment.newInstance(idLeague),
        TableFragment.newInstance(idLeague),
        TeamFragment.newInstance(idLeague)
    )

    private fun leagueDetailPagesTitle(context: Context): List<String> = listOf(
        context.getString(R.string.label_match),
        context.getString(R.string.label_standings),
        context.getString(R.string.label_team)
    )
}
