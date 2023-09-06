package com.example.news

import org.json.JSONObject

/**
 * A data structure containing a list of all possible topic names
 */
class NewsModel {
    private var Topics : MutableList<String> = mutableListOf<String>()

    constructor() {
        addAllPossibleTopics()
    }

    fun addTopicName(j : String) {
        Topics.add(j)
    }

    fun getTopicName(i : Int) : String {
        return Topics.get(i)
    }

    fun getNumTopics() : Int {
        return Topics.size
    }

    fun addAllPossibleTopics() {
        addTopicName("news")
        addTopicName("sport")
        addTopicName("tech")
        addTopicName("world")
        addTopicName("finance")
        addTopicName("politics")
        addTopicName("business")
        addTopicName("economics")
        addTopicName("entertainment")
        addTopicName("beauty")
        addTopicName("travel")
        addTopicName("music")
        addTopicName("food")
        addTopicName("science")
        addTopicName("gaming")
        addTopicName("energy")
    }




}