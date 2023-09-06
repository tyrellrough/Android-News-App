package com.example.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A fragment that displays the users history and their account information
 */
class MeFragment : Fragment() {
    private lateinit var UID : String
    private lateinit var email : String
    private lateinit var historyRecycler : RecyclerView
    private lateinit var adapter: MeAdapter
    private lateinit var historyDB : HistoryDBManager
    private lateinit var historyModel : HistoryModel

    override fun onCreate(savedInstanceState: Bundle?) {
        UID = arguments?.getString("UID").toString()
        email = arguments?.getString("email").toString()
        historyDB = HistoryDBManager(requireContext())
        historyModel = historyDB.getHistory(UID)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyRecycler = view.findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyRecycler.layoutManager = LinearLayoutManager(context)
        adapter = MeAdapter()
        historyRecycler.adapter = adapter
        val emailTxt = view.findViewById<TextView>(R.id.txtAccountName)
        val logoutBtn = view.findViewById<Button>(R.id.btnLogout)
        emailTxt.text = email

        logoutBtn.setOnClickListener {
            logout()
        }
    }

    /**
     * loads an article using the url
     * @param URL the url of the article to load
     */
    fun loadArticle(URL : String) {
        (activity as MainActivity).loadArticleView(URL)
    }

    /**
     * loads the login actitivy
     */
    fun logout() {
        (activity as MainActivity).logout()
    }

    companion object {
        @JvmStatic
        fun newInstance(UID : String, email : String) =
            MeFragment().apply {
                arguments = Bundle().apply {
                    putString("UID",UID)
                    putString("email", email)
                }
            }
    }

    /**
     * Fills the history recycler with the current users article history
     */
    inner class MeAdapter() : RecyclerView.Adapter<MeAdapter.ViewHolder>() {
        inner class ViewHolder(var layout:View) : RecyclerView.ViewHolder(layout) {
            val txtPublisher = itemView.findViewById<View>(R.id.txtPublisherName) as TextView
            val txtTitle = itemView.findViewById<View>(R.id.txtArticleTitle) as TextView

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.history_row, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtPublisher.text = historyModel.getHistoryRow(position).getPublisherName()
            holder.txtTitle.text = historyModel.getHistoryRow(position).getTitle()
            val URL = historyModel.getHistoryRow(position).getArticleURL()

            holder.itemView.setOnClickListener {
                loadArticle(URL)
            }
        }

        override fun getItemCount(): Int {
            return historyModel.getSize()
        }
    }



}