package com.example.news

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Responsible for managing a database of topics containing every uses
 * topics. A topic has a name, following status, notification status and user id.
 */
class TopicsDBManager(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TASKS_TABLE =
            "CREATE	TABLE $TABLE_TOPICS($ID_COL INTEGER PRIMARY KEY,$UID_COL STRING, $TOPIC_COL STRING,$IS_FOLLOWING_COL STRING, $IS_NOTIFIED_COL STRING)"
        db.execSQL(CREATE_TASKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TOPICS")
        onCreate(db)
    }

    /**
     * Adds a new user to the database with three topics already followed.
     * @param UID current user id
     * @param topics a default topics object
     */
    fun addNewUser(UID : String, topics : Topics){
        val db = this.writableDatabase
        val values = ContentValues()
        var s : String? = ""
        for(key in topics.getAllPossibleTopics().keys) {
            values.put(UID_COL,UID)
            s = topics.getTopic(key)?.getName()
            Log.d("user adding", s.toString())
            values.put(TOPIC_COL,s)
            s = ""
            s = topics.getTopic(key)?.getFollowingStatus()
            Log.d("user adding", s.toString())
            values.put(IS_FOLLOWING_COL,s)
            s = ""
            s = topics.getTopic(key)?.getNotificationStatus()
            Log.d("user adding", s.toString())
            values.put(IS_NOTIFIED_COL,s)
            db.insert(TABLE_TOPICS,null,values)
        }
    }

    /**
     * Returns the users topics and if the user is following that topic and
     * if the user has notifications turned on for that topic.
     * @param UID the user id you want to get topics of
     * @return the users topics
     */
    fun getUsersTopics(UID: String) : Topics {
        val query = "SELECT * FROM $TABLE_TOPICS WHERE $UID_COL = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(UID))
        var topics = Topics()
        if(cursor.moveToFirst()) {
            do {
                val topicName = cursor.getString(2)
                Log.d("get user topics", topicName)
                val isFollowed = cursor.getString(3)
                Log.d("get user topics", isFollowed)
                val isNotificationOn = cursor.getString(4)
                Log.d("get user topics", isNotificationOn)
                topics.getTopic(topicName)?.setFollowingStatus(isFollowed)
                topics.getTopic(topicName)?.setIsNotificationStatus(isNotificationOn)
                cursor.moveToNext()
            }while(cursor.moveToNext())
        }
        return topics
    }

    /**
     * gets a single topic
     * @param UID user id
     * @param topicName name of the topic you want
     */
    fun getTopic(UID : String, topicName : String) : Topics.Topic  {
        val query = "SELECT * FROM $TABLE_TOPICS WHERE $UID_COL = ? AND $TOPIC_COL = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(UID,topicName))
        var topic : Topics.Topic = Topics.Topic("empty","false","false")
        if(cursor.moveToFirst()){
            do{
                val name = cursor.getString(2)
                Log.d("gettopic",name)
                val followingStatus = cursor.getString(3)
                Log.d("gettopic",followingStatus)
                val notifyStatus = cursor.getString(4)
                Log.d("gettopic",notifyStatus)
                topic = Topics.Topic(name,followingStatus,notifyStatus)
            }while (cursor.moveToNext())
        }

        return topic
    }

    /**
     * Changes values of a topic in the database
     * @param UID user id
     * @param topicName name of topic you want to change
     * @param isFollowed new isFollowed value "true" or "false" (string)
     * @param isNotified new isNotified values "true" or "false" (string)
     */
    fun updateTopic(UID: String, topicName: String, isFollowed: String, isNotified: String) {
        val query = "$UID_COL = ? AND $TOPIC_COL = ?"
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(UID_COL, UID)
        values.put(TOPIC_COL, topicName)
        values.put(IS_FOLLOWING_COL, isFollowed)
        values.put(IS_NOTIFIED_COL, isNotified)

        db.update(TABLE_TOPICS, values,query, arrayOf(UID,topicName))
    }

    /**
     * Gets the users followed topics
     * @param UID user id
     * @return a topics object containing all followed topics
     */
    fun getFollowedTopics(UID : String) : Topics {
        val query = "SELECT * FROM $TABLE_TOPICS WHERE $UID_COL = ? AND $IS_FOLLOWING_COL = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(UID,"true"))
        var topics = Topics()
        topics.setAllFollowingFalse()
        if(cursor.moveToFirst()){
            do{
                val name = cursor.getString(2)
                //val followingStatus = cursor.getString(3)
                //val notifyStatus = cursor.getString(4)
                topics.getTopic(name)?.setFollowingStatus("true")
            }while (cursor.moveToNext())
        }

        return topics
    }

    companion object{
        private val DATABASE_NAME = "databasetopics"
        private val DATABASE_VERSION = 1
        val TABLE_TOPICS = "tableTopics"
        val ID_COL = "id"
        val UID_COL = "uid"
        val TOPIC_COL = "topic"
        val IS_FOLLOWING_COL = "isFollowing"
        val IS_NOTIFIED_COL = "isNotified"
    }
}