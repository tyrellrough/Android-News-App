package com.example.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koushikdutta.ion.Ion
import com.squareup.picasso.Picasso
import org.json.JSONObject

/**
 * A fragment which displays the news.
 */
class NewsTopicFragment : Fragment() {
    private lateinit var adapter: NewsAdapter
    private lateinit var topicName: String
    private lateinit var UID : String
    private lateinit var historyDB : HistoryDBManager
    private lateinit var newsJsonObject: JSONObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topicName = arguments?.getString("topicName").toString()
        UID = arguments?.getString("UID").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_topic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNewsTopic(view)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsTopicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(topicName: String, UID : String) =
            NewsTopicFragment().apply {
                arguments = Bundle().apply {
                    putString("topicName", topicName)
                    putString("UID",UID)
                }
            }
    }

    fun getNewsTopic(v : View) {
        Log.d("1610","getnewstopic")
        val apiKey = "0f230900380aa1eca6227327cf31bb43"
            //"284de49bcfc9896ac2c1057a51cfc8bb"
        val uri = "https://gnews.io/api/v4/search?q=" + topicName +"&token=" + apiKey + "&lang=en&country=us&max=10"
        Log.d("1610","news query + " + uri)
        Ion.with(this)
            .load(uri)
            .setHeader("x-api-key",apiKey)
            .asString()
            .setCallback {ex, result ->
                processNews(result, v)
            }
    }

    fun processNews(newsData : String, v : View)  {
        newsJsonObject = JSONObject(newsData)
        val newsRecyclerView = v.findViewById<View>(R.id.newsTopicRecycler) as RecyclerView
        newsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = NewsAdapter(newsJsonObject)
        newsRecyclerView.adapter = adapter
    }

    fun loadArticle(URL : String) {
        (parentFragment as HomeFragment).loadArticleView(URL)
    }

    inner class NewsAdapter(myJSONObject: JSONObject) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
        var myJson = myJSONObject

        inner class ViewHolder(layout:View) : RecyclerView.ViewHolder(layout) {
            val txtPublisher = itemView.findViewById<View>(R.id.publisher_name) as TextView
            val txtTitle = itemView.findViewById<View>(R.id.title) as TextView
            val imageView = itemView.findViewById<View>(R.id.article_picture) as ImageView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.news_row, parent, false)

            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtPublisher.text = myJson.getJSONArray("articles").getJSONObject(position).getJSONObject("source").getString("name")
            holder.txtTitle.text = myJson.getJSONArray("articles").getJSONObject(position).getString("title").toString()
            val imageURL = myJson.getJSONArray("articles").getJSONObject(position).getString("image").toString()
            Picasso.get().load(imageURL).into(holder.imageView)

            holder.itemView.setOnClickListener {
                val articleURL = myJson.getJSONArray("articles").getJSONObject(position).getString("url").toString()
                historyDB = HistoryDBManager(requireContext())
                val publisher = myJson.getJSONArray("articles").getJSONObject(position).getJSONObject("source").getString("name")
                val title = myJson.getJSONArray("articles").getJSONObject(position).getString("title").toString()
                historyDB.addHistoryRow(UID,publisher,title,articleURL)
                loadArticle(articleURL)
            }

        }


        override fun getItemCount(): Int {
            return 10;
            //max api articles is 10
        }

    }
}