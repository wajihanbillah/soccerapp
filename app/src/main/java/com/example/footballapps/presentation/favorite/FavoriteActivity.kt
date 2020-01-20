package com.example.footballapps.presentation.favorite

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.footballapps.R
import com.example.footballapps.presentation.adapter.GeneralPagerAdapter
import com.example.footballapps.utils.FavoriteType
import com.example.footballapps.utils.setupToolbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.jetbrains.anko.startActivity

class FavoriteActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity<FavoriteActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setupToolbar(toolbar, getString(R.string.label_favorite), true)

        initViewPagerFavorite()
    }

    private fun initViewPagerFavorite() {
        vpFavorite.adapter =
            GeneralPagerAdapter(
                supportFragmentManager,
                pages = favoritePages(),
                title = favoritePagesTitle(this)
            )
        vpFavorite.offscreenPageLimit = 2
        tabFavorite.setupWithViewPager(vpFavorite)
    }

    private fun favoritePages(): List<Fragment> = listOf(
        FavoriteListFragment.newInstance(FavoriteType.FAVORITE_EVENT.type),
        FavoriteListFragment.newInstance(FavoriteType.FAVORITE_TEAM.type)
    )

    private fun favoritePagesTitle(context: Context): List<String> = listOf(
        context.getString(R.string.label_event),
        context.getString(R.string.label_team)
    )

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }
}
