package com.example.news

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * A class for managing a database which stores
 * article history for all users.
 */
class HistoryDBManager(context: Context) :
    SQLiteOpenHelper(context, TABLE_NAME, null, DATABASE_VERSION) {

    companion object{
        private val DATABASE_NAME = "databaseHistory"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "table_history"
        val ID_COL = "id"
        val UID_COL = "uid"
        val PUBLISHER_COL = "publisher"
        val TITLE_COL = "isFollowing"
        val URL_COL = "isNotified"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TASKS_TABLE =
            "CREATE TABLE $TABLE_NAME($ID_COL INTEGER PRIMARY KEY,$UID_COL STRING, $PUBLISHER_COL STRING,$TITLE_COL STRING, $URL_COL STRING)"
        db.execSQL(CREATE_TASKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * Adds a history row to the database.
     * @param UID the user id
     * @param publisher the name of the publisher
     * @param title the title of the article
     * @param URL the url of the article
     */
    fun addHistoryRow(UID : String, publisher : String, title : String, URL : String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(UID_COL,UID)
        values.put(PUBLISHER_COL,publisher)
        values.put(TITLE_COL,title)
        values.put(URL_COL,URL)
        db.insert(TABLE_NAME,null,values)
    }

    /**
     * Returns all history of the specified user.
     * @param UID the user id
     * @return History Model, containing all the articles the user has clicked
     */
    fun getHistory(UID: String) : HistoryModel {
        val query = "SELECT * FROM $TABLE_NAME WHERE $UID_COL = ?"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, arrayOf(UID))
        var history = HistoryModel()
        if(cursor.moveToFirst()) {
            do {
                val publisher = cursor.getString(2)
                val title = cursor.getString(3)
                val url = cursor.getString(4)
                val historyRow = HistoryModel.HistoryRow(publisher,title,url)
                history.addHistoryRow(historyRow)
                cursor.moveToNext()
            }while(cursor.moveToNext())
        }
        return history
    }

}