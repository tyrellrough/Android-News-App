package com.example.news

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * An activity for logging in and registering.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var loginButton : Button;
    private lateinit var registerButton : Button
    private var mAuth = FirebaseAuth.getInstance()
    private var currentUser = mAuth.currentUser
    private lateinit var topicsDB : TopicsDBManager
    private lateinit var topics : Topics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        topicsDB = TopicsDBManager(this)

        mAuth = Firebase.auth

        emailText = findViewById(R.id.editTextTextEmailAddress)
        passwordText = findViewById(R.id.editTextTextPassword)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)


        loginButton.setOnClickListener{v -> loginCLick(v)}
        registerButton.setOnClickListener{v -> registerCLick(v)}
        mAuth.signOut()

    }

    /**
     * Prevents the user from pressing the back button
     * to load into the app after logging out.
     */
    override fun onBackPressed() {
        //do nothing
    }

    override fun onStart() {
        super.onStart()
        currentUser = mAuth.currentUser
        update()
    }

    private fun registerCLick(v: View) {
        if((emailText.text.toString() != "") and (passwordText.text.toString() !="")) {
            if (mAuth.currentUser != null) {
                displayMessage(registerButton, getString(R.string.register_while_logged_in))
            } else {
                mAuth.createUserWithEmailAndPassword(
                    emailText.text.toString(),
                    passwordText.text.toString()
                ).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        closeKeyboard()
                        update()
                        val defaultTopics = Topics()
                        topicsDB.addNewUser(currentUser?.uid.toString(),defaultTopics)
                        topics = topicsDB.getUsersTopics(currentUser?.uid.toString())
                    } else {
                        closeKeyboard()
                    }

                }
            }
        }
    }

    fun loginCLick(v: View) {
        if((emailText.text.toString() != "") and (passwordText.text.toString() !="")) {
            mAuth.signInWithEmailAndPassword(
                emailText.text.toString(),
                passwordText.text.toString()
            ) .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    closeKeyboard()
                    update()
                    val uid : String? = mAuth.currentUser?.uid
                    loadMainActivity(uid)
                } else {
                    closeKeyboard()
                    displayMessage(loginButton, getString(R.string.login_failure))

                }
            }
        }
    }

    fun update() {
        currentUser = mAuth.currentUser
    }

    fun displayMessage(view: View, msgTxt : String) {
        val snackbar = Snackbar.make(view, msgTxt, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if(view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }
    }

    /**
     * Loads the main activity
     */
    private fun loadMainActivity(uid : String?) {
        val newIntent = Intent(this,MainActivity::class.java)
        newIntent.putExtra("UID",uid)
        newIntent.putExtra("email",mAuth.currentUser?.email.toString())
        startActivity(newIntent)
    }





}