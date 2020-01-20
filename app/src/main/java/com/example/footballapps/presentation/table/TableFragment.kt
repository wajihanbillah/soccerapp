package com.example.footballapps.presentation.table


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapps.R
import com.example.footballapps.datagson.ApiRepository
import com.example.footballapps.model.Table
import com.example.footballapps.model.Team
import com.example.footballapps.presentation.adapter.TableAdapter
import com.example.footballapps.utils.BundleKeys
import com.example.footballapps.utils.emptyString
import com.google.gson.Gson
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.fragment_table.*

class TableFragment : Fragment(), TableViews {

    private var idLeague = emptyString()

    private lateinit var tablePresenter: TablePresenter

    private lateinit var tableAdapter: TableAdapter

    private var listTable = mutableListOf<Table>()

    private var listTeamId = mutableListOf<String>()

    private var position = 0

    companion object {
        fun newInstance(
            idLeague: String
        ): TableFragment {
            val fragment = TableFragment()
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
        return inflater.inflate(R.layout.fragment_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            idLeague = it.getString(BundleKeys.LEAGUE_ID) ?: emptyString()
        }

        val request = ApiRepository()
        val gson = Gson()
        tablePresenter = TablePresenter(this, gson, request)

        context?.let {
            tableAdapter = TableAdapter(it)
        }

        rvTable.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            setHasFixedSize(true)
            adapter = tableAdapter
        }

        tablePresenter.getTableList(idLeague)
    }

    override fun showLoading() {
        msvTable.viewState = MultiStateView.ViewState.LOADING
    }

    override fun showTableList(data: List<Table>) {
        if (data.isEmpty()) {

            msvTable.viewState = MultiStateView.ViewState.EMPTY
        } else {
            listTable.clear()
            listTeamId.clear()

            listTable = data.toMutableList()

            tableAdapter.setTableData(listTable)


            listTable.forEachIndexed { _, event ->
                listTeamId.add(event.teamid)
            }

            msvTable.viewState = MultiStateView.ViewState.CONTENT

            tablePresenter.getTeamDetail(listTeamId[position])
        }
    }

    override fun showTeamDetail(data: List<Team>) {
        listTable[position].strTeamBadge = data[0].strTeamBadge

        tableAdapter.addOrUpdate(listTable[position])

        if (position < listTable.size - 1) {
            position++

            tablePresenter.getTeamDetail(listTeamId[position])
        }
    }
}
