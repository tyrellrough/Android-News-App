package com.example.news

/**
 * A data structure for storing a users article history
 */
class HistoryModel {
    /**
     * List of articles
     */
    private var historyRows : MutableList<HistoryRow> = mutableListOf<HistoryRow>()

    /**
     * Add an article
     */
    fun addHistoryRow(historyRow: HistoryRow) {
        historyRows.add(historyRow)
    }

    /**
     * Get an article
     */
    fun getHistoryRow(i : Int) : HistoryRow {
        return historyRows.get(i)
    }

    fun getSize() : Int {
        return historyRows.size
    }

    /**
     * A data structure for storing an article the user clicks on
     */
    class HistoryRow(publisher : String,title : String, url : String) {
        private var publisherName : String = publisher
        private var title : String = title
        private var articleURL : String = url

        fun getPublisherName() : String {
            return publisherName
        }

        fun getTitle() : String {
            return title
        }

        fun getArticleURL() : String {
            return articleURL
        }


    }
}