package com.example.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A fragment responsible for displaying topics which the user
 * can follow.
 */
class ExploreFragment : Fragment() {
    private lateinit var topicsDB : TopicsDBManager
    private lateinit var newsModel : NewsModel
    private lateinit var UID : String
    private lateinit var userTopics : Topics
    private lateinit var exploreRecycler : RecyclerView
    private lateinit var adapter: ExploreAdapter


    companion object {
        @JvmStatic
        fun newInstance(UID : String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString("UID", UID)
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UID = arguments?.getString("UID").toString()
        topicsDB = TopicsDBManager(requireContext())
        userTopics = topicsDB.getUsersTopics(UID)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exploreRecycler = view.findViewById<View>(R.id.exploreRecyclerView) as RecyclerView
        exploreRecycler.layoutManager = LinearLayoutManager(context)
        adapter = ExploreAdapter()
        exploreRecycler.adapter = adapter
        newsModel = NewsModel()
    }

    inner class ExploreAdapter() : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {

        inner class ViewHolder(var layout:View) : RecyclerView.ViewHolder(layout) {
            val txtTopicName = itemView.findViewById<View>(R.id.textewsTopic) as TextView
            val followButton = itemView.findViewById<View>(R.id.buttonFollow) as Button
            val notifyButton = itemView.findViewById<View>(R.id.buttonNotify) as Button
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.explore_row, parent, false)

            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentTopicName = newsModel.getTopicName(position)
            holder.txtTopicName.text = currentTopicName
            val currentTopic = topicsDB.getTopic(UID,currentTopicName)
            var currentTopicFollowingStatus = currentTopic.getFollowingStatus()
            var currentTopicNotifyStatus = currentTopic.getNotificationStatus()

            if(currentTopicFollowingStatus == "true") {
                holder.followButton.text = "Unfollow"
            } else {
                holder.followButton.text = "Follow"
            }
            if(currentTopicNotifyStatus == "true") {
                holder.notifyButton.text = "Turn off notifcations"
            } else {
                holder.notifyButton.text = "Turn on notifications"
            }

            holder.followButton.setOnClickListener {
                Log.d("clickevent","followbutton pos" + position)
                if(currentTopicFollowingStatus == "true") {
                    topicsDB.updateTopic(UID,currentTopicName,"false",currentTopicNotifyStatus)

                    holder.followButton.text = "Follow"
                    currentTopicFollowingStatus = "false"

                } else {
                    topicsDB.updateTopic(UID,currentTopicName,"true",currentTopicNotifyStatus)

                    holder.followButton.text = "Unfollow"
                    currentTopicFollowingStatus = "true"
                }

            }

            holder.notifyButton.setOnClickListener {
                Log.d("clickevent","notifybutton pos" + position)
                if(currentTopicNotifyStatus == "true") {
                    topicsDB.updateTopic(UID,currentTopicName,currentTopicFollowingStatus,"false")

                    holder.notifyButton.text = "Turn on notifications"
                    currentTopicNotifyStatus = "false"

                } else {
                    topicsDB.updateTopic(UID,currentTopicName,currentTopicFollowingStatus,"true")

                    holder.notifyButton.text = "Turn off notifications"
                    currentTopicNotifyStatus= "true"
                }
            }

        }

        override fun getItemCount(): Int {
            return 16;
            //there's 16 topics
        }

    }
}