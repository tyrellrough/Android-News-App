package com.example.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationBar : BottomNavigationView
    private var currentUserUID : String? = ""
    private lateinit var topicsDB : TopicsDBManager
    private var currentUserEmail : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentUserUID = intent.getStringExtra("UID")
        currentUserEmail = intent.getStringExtra("email")
        Log.d("current uid", currentUserUID.toString())
        bottomNavigationBar = findViewById(R.id.bottom_nav_bar)

        bottomNavSwitcher(currentUserUID.toString(),currentUserEmail.toString())
        setFragment(HomeFragment.newInstance(currentUserUID.toString()))
    }


    /**
     * Switches the displayed fragment when the user presses a button on the bottom
     * navigation bar
     * @param currentUserUID the current user id
     * @param currentUserEmail the current user email
     */
    private fun bottomNavSwitcher(currentUserUID : String,currentUserEmail : String) {
        bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    setFragment(HomeFragment.newInstance(currentUserUID.toString()))
                    true
                }
                R.id.explore -> {
                    setFragment(ExploreFragment.newInstance(currentUserUID.toString()))
                    true
                }
                R.id.account -> {
                    setFragment(MeFragment.newInstance(currentUserUID.toString(),currentUserEmail))
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    /**
     * Sets a fragment
     * @param fragment the fragment to load
     */
    private fun setFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView,fragment)
        transaction.commit()
    }

    /**
     * Loads an article
     */
    fun loadArticleView(URL : String) {
        val intent = Intent(this,ArticleActivity::class.java)
        intent.putExtra("URL",URL)
        startActivity(intent)
    }

    /**
     *Loads the login activity
     */
    fun logout() {
        val intent = Intent(this,LoginActivity :: class.java)
        startActivity(intent)
    }


}


