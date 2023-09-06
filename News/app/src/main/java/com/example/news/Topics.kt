package com.example.news

import android.util.Log

private const val TOPIC_NEWS : String = "news"
private const val TOPIC_SPORT : String = "sport"
private const val TOPIC_TECH : String = "tech"
private const val TOPIC_WORLD : String = "world"
private const val TOPIC_FINANCE : String = "finance"
private const val TOPIC_POLITICS : String = "politics"
private const val TOPIC_BUSINESS : String = "business"
private const val TOPIC_ECONOMICS : String = "economics"
private const val TOPIC_ENTERTAINMENT : String = "entertainment"
private const val TOPIC_BEAUTY : String = "beauty"
private const val TOPIC_TRAVEL : String = "travel"
private const val TOPIC_MUSIC : String = "music"
private const val TOPIC_FOOD : String = "food"
private const val TOPIC_SCIENCE : String = "science"
private const val TOPIC_GAMING : String = "gaming"
private const val TOPIC_ENERGY : String = "energy"

/**
 * A data structure containing all topics and their information.
 * It is used to store information from topics database.
 */
class Topics {
    var topicsHashMap : HashMap<String,Topic> = HashMap<String,Topic>()

    constructor() {
        var topicNews : Topic = Topic(TOPIC_NEWS,"true","false")
        var topicSport : Topic = Topic(TOPIC_SPORT,"true","false")
        var topicTech : Topic = Topic(TOPIC_TECH,"true","false")
        var topicWorld : Topic = Topic(TOPIC_WORLD,"false","false")
        var topicFinance : Topic = Topic(TOPIC_FINANCE,"false","false")
        var topicPolitics : Topic = Topic(TOPIC_POLITICS,"false","false")
        var topicBusiness : Topic = Topic(TOPIC_BUSINESS,"false","false")
        var topicEconomics : Topic = Topic(TOPIC_ECONOMICS,"false","false")
        var topicEntertainment : Topic = Topic(TOPIC_ENTERTAINMENT,"false","false")
        var topicBeauty : Topic = Topic(TOPIC_BEAUTY,"false","false")
        var topicTravel : Topic = Topic(TOPIC_TRAVEL,"false","false")
        var topicMusic : Topic = Topic(TOPIC_MUSIC,"false","false")
        var topicFood : Topic = Topic(TOPIC_FOOD,"false","false")
        var topicScience : Topic = Topic(TOPIC_SCIENCE,"false","false")
        var topicGaming : Topic = Topic(TOPIC_GAMING,"false","false")
        var topicEnergy : Topic = Topic(TOPIC_ENERGY,"false","false")

        addTopic(topicNews)
        addTopic(topicSport)
        addTopic(topicTech)
        addTopic(topicWorld)
        addTopic(topicFinance)
        addTopic(topicPolitics)
        addTopic(topicBusiness)
        addTopic(topicEconomics)
        addTopic(topicEntertainment)
        addTopic(topicBeauty)
        addTopic(topicTravel)
        addTopic(topicMusic)
        addTopic(topicFood)
        addTopic(topicScience)
        addTopic(topicGaming)
        addTopic(topicEnergy)
    }

    fun setAllFollowingFalse() {
        topicsHashMap.forEach{t ->
            t.value.setFollowingStatus("false")
        }
    }

    fun getAllPossibleTopics() : HashMap<String,Topic> {
        return topicsHashMap
    }

    private fun addTopic(t : Topic) {
        topicsHashMap.put(t.getName(),t)
    }

    fun getTopic(key : String) : Topic? {
        return topicsHashMap.get(key)!!
    }

    fun getNameAtIndex(i : Int) : String {
        val topicName = ""
        when(i) {
            0 -> return TOPIC_NEWS
            1 -> return TOPIC_TECH
            2 -> return TOPIC_WORLD
            3 -> return TOPIC_FINANCE
            4 -> return TOPIC_POLITICS
            5 -> return TOPIC_BUSINESS
            6 -> return TOPIC_ECONOMICS
            7 -> return TOPIC_ENTERTAINMENT
            8 -> return TOPIC_BEAUTY
            9 -> return TOPIC_TRAVEL
            10 -> return TOPIC_MUSIC
            11 -> return TOPIC_FOOD
            12 -> return TOPIC_SCIENCE
            13 -> return TOPIC_GAMING
            14 -> return TOPIC_ENERGY
        }
        return "news"
    }

    /**
     * A data structure containing the following status, name, notification status of
     * a topic
     */
    class Topic(name : String, isFollowed : String, isNotificationsOn : String) {
        private var name = name
        private var isFollowed = isFollowed
        private var isNotificationsOn = isNotificationsOn

        fun getName() : String {
            return name
        }

        fun getFollowingStatus() : String {
            return isFollowed
        }

        fun getNotificationStatus() : String {
            return isNotificationsOn
        }

        fun setFollowingStatus(s : String) {
            isFollowed = s
        }

        fun setIsNotificationStatus(s : String) {
            isNotificationsOn = s
        }


    }


}