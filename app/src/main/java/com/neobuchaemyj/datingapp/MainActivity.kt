package com.neobuchaemyj.datingapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Fragments.MainInfoFragment
import com.neobuchaemyj.datingapp.Model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var facebookLogin: LoginButton

    lateinit var textView: TextView

    lateinit var callbackManager: CallbackManager

    lateinit var intent1:Intent
    private var user = User()
    private lateinit var db: AppDatabase
    var userEmail: String = "didn't get"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        facebookLogin = findViewById(R.id.facebook_login_button)

        facebookLogin.setReadPermissions("email")

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object: FacebookCallback<LoginResult>{

            override fun onError(error: FacebookException?) {

            }

            override fun onCancel() {
                Toast.makeText(this@MainActivity, "Login cancelled", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: LoginResult) {
                isUserInDb(getUserEmail(result.accessToken))
            }

        })

        facebookLogin.setOnClickListener {
            if (AccessToken.getCurrentAccessToken() == null) {
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getUserEmail(token: AccessToken): String {

        var request: GraphRequest = GraphRequest.newMeRequest(
            token
        ) { `object`, response ->
            Log.v("MainActivity", response.toString())

            if (`object` != null) {
                userEmail = `object`.getString("email")
            }
        }

        var parameters: Bundle = Bundle()
        parameters.putString("email", "email")
        request.parameters = parameters
        request.executeAsync()
        return userEmail
    }

    fun onCreateAccButtonClick(v:View) {

        intent1 = Intent(this, QuestionnaireActivity::class.java)
        intent1.putExtra("action", "registration")
        startActivity(intent1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("CheckResult")
    fun saveToDb(user: User) {

        Completable.fromAction { db.userDao().insert(user) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })

    }

    @SuppressLint("CheckResult")
    fun isUserInDb(email: String) {

        Observable.fromCallable{ db.userDao().isEmailInDb(email) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it>0) {
                    Toast.makeText(this, "That's email already exist", Toast.LENGTH_SHORT).show()
                } else {
                    user.setEmail(email)
                    saveToDb(user)
                    intent1 = Intent(this, QuestionnaireActivity::class.java)
                    intent1.putExtra("action", "facebook_login")
                    startActivity(intent1)
                }
            }

    }
}
