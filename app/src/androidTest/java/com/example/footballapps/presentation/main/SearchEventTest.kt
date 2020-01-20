package com.example.footballapps.presentation.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.footballapps.R
import com.example.footballapps.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchEventTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    @Test
    fun testRecyclerViewBehaviour() {

        onView(withId(R.id.rvLeagueList))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rvLeagueList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.menuSearch))
            .check(matches(isDisplayed()))
        onView(withId(R.id.menuSearch)).perform(click())

        onView(withId(R.id.menuSearchView))
            .check(matches(isDisplayed()))

        onView(withId(R.id.menuSearchView)).perform(click())

        searchQuery("Racing Club")
        searchQuery("Godoy Cruz")
        searchQuery("River Plate")
    }

    private fun searchQuery(query: String) {
        onView(withId(R.id.search_src_text)).perform(clearText())
        onView(withId(R.id.search_src_text)).perform(typeText(query))
        onView(withId(R.id.search_src_text)).perform(pressImeActionButton())
    }
}