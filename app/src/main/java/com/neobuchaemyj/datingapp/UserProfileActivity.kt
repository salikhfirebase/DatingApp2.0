package com.neobuchaemyj.datingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Fragments.MainInfoFragment
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.app_bar_user_profile.*

class UserProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var db: AppDatabase
    var userId = 0
    var userEmail = ""
    lateinit var userName: TextView

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        userId = intent.getIntExtra("reg", 0)
        userEmail = intent.getStringExtra("log_in")
        userName = findViewById(R.id.user_profile_greet)
        db = AppDatabase.getInstance(this) as AppDatabase

        if (userId != 0) {
            Observable.fromCallable{ db.userDao().getUserFromDbById(userId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    userName.text = "Hi, ${it.getNick()}"
                }
        }

        if (userEmail != "") {
            Observable.fromCallable{ db.userDao().getUserFromDb(userEmail) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    userName.text = it.getNick()
                }
        }

    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_messages -> {
                // Handle the camera action
            }
            R.id.nav_profile -> {

            }
            R.id.nav_chat -> {

            }
            R.id.nav_free_stars -> {

            }
            R.id.nav_feedback -> {

            }
            R.id.nav_help -> {

            }

            R.id.nav_sign_out -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
