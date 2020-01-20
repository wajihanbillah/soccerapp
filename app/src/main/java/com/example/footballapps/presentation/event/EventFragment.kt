package com.example.footballapps.presentation.event


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.footballapps.R
import com.example.footballapps.presentation.adapter.GeneralPagerAdapter
import com.example.footballapps.utils.BundleKeys
import com.example.footballapps.utils.emptyString
import com.example.footballapps.utils.enum.EventType
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : Fragment() {

    private var idLeague = emptyString()

    companion object {
        fun newInstance(
            idLeague: String
        ): EventFragment {
            val fragment = EventFragment()
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
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            idLeague = it.getString(BundleKeys.LEAGUE_ID) ?: emptyString()
        }

        initViewPagerEvent()
    }

    private fun initViewPagerEvent() {
        context?.let {
            vpEvent.adapter =
                GeneralPagerAdapter(
                    childFragmentManager,
                    pages = eventPages(idLeague),
                    title = eventPagesTitle(it)
                )
            vpEvent.offscreenPageLimit = 2
            tabEvent.setupWithViewPager(vpEvent)
        }
    }

    private fun eventPages(idLeague: String): List<Fragment> = listOf(
        EventListFragment.newInstance(EventType.LAST_MATCH.type, idLeague),
        EventListFragment.newInstance(EventType.NEXT_MATCH.type, idLeague)
    )

    private fun eventPagesTitle(context: Context): List<String> = listOf(
        context.getString(R.string.label_last_match),
        context.getString(R.string.label_next_match)
    )
}
