package com.example.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * A fragment responsible for creating tabs which display the news
 */
class HomeFragment : Fragment() {
    private lateinit var newsViewPager : ViewPager2
    private lateinit var tabLayout : TabLayout
    private lateinit var topicsDB: TopicsDBManager
    private lateinit var followedTopics : Topics
    private lateinit var UID : String
    var followedTopicsNameList : MutableList<String> =  mutableListOf<String>()

    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topicsDB = TopicsDBManager(requireContext())
        UID = arguments?.getString("UID").toString()
        val topics = topicsDB.getUsersTopics(UID)
        followedTopics = topicsDB.getFollowedTopics(UID)
        followedTopics.topicsHashMap.forEach{t ->
            if(t.value.getFollowingStatus() == "true") {
                followedTopicsNameList.add(t.value.getName())
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewPager = view.findViewById(R.id.newsViewPager2)
        tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        newsViewPager.adapter = NewsTabAdapter(this)
        nameTabs()
    }

    /**
     * Names the tabs using the followed topics
     */
    fun nameTabs() {
        TabLayoutMediator(tabLayout,newsViewPager) { tab, position ->
            tab.text = followedTopicsNameList.get(position)
        }.attach()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(UID : String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("UID",UID)
                }
            }
    }

    /**
     * Loads an article using a given url
     * @param URL url of article to load
     */
    fun loadArticleView(URL : String) {
        (activity as MainActivity).loadArticleView(URL)
    }

    /**
     * Tab adapter for the tab layout and viewpager
     */
    inner class NewsTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return followedTopicsNameList.size
            //followedTopics.size

        }
        override fun createFragment(position: Int): Fragment {
            val fragment = NewsTopicFragment.newInstance(followedTopicsNameList.get(position),UID)

            return fragment
        }
    }
}