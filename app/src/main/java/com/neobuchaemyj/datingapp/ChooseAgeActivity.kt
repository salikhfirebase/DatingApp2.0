package com.neobuchaemyj.datingapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.neobuchaemyj.datingapp._core.BaseActivity
import com.neobuchaemyj.datingapp.ui.WebViewActivity

class ChooseAgeActivity : BaseActivity() {

    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var toWebViewButton: Button
    lateinit var intent1: Intent
    private lateinit var remoteConfig1: FirebaseRemoteConfig
    val APP_REFERENCES = "mysettings"
    val APP_REFERENCES_AGE = "empty"
    lateinit var mSettings: SharedPreferences
    lateinit var mEditor: SharedPreferences.Editor
    var sp: String? = null


    override fun setUI() {

        fetchRemoteConfig({
            remoteConfig1 = it

            val taskUrl25 = remoteConfig1.getString(TASK_URL_25)
            val taskUrl2529 = remoteConfig1.getString(TASK_URL_25_29)
            val taskUrl30 = remoteConfig1.getString(TASK_URL_30)

            if (mSettings.contains(APP_REFERENCES_AGE)) {
                sp = mSettings.getString(APP_REFERENCES_AGE, "empty")
            }

            when (sp) {
                "меньше 25" -> {
                    intent1.putExtra(EXTRA_TASK_URL, taskUrl25)
                    startActivity(intent1)
                }
                "25-29" -> {
                    intent1.putExtra(EXTRA_TASK_URL, taskUrl2529)
                    startActivity(intent1)
                }
                "больше 30" -> {
                    intent1.putExtra(EXTRA_TASK_URL, taskUrl30)
                    startActivity(intent1)
                }
            }

            toWebViewButton.setOnClickListener {
                radioButton = findViewById(radioGroup.checkedRadioButtonId)

                when (radioButton.text.toString()) {

                    "меньше 25" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "меньше 25")
                        mEditor.apply()
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl25)
                        startActivity(intent1)
                    }
                    "25-29" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "25-29")
                        mEditor.apply()
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl2529)
                        startActivity(intent1)
                    }
                    "больше 30" -> {
                        mEditor.putString(APP_REFERENCES_AGE, "больше 30")
                        mEditor.apply()
                        intent1.putExtra(EXTRA_TASK_URL, taskUrl30)
                        startActivity(intent1)
                    }

                }
            }
        })

    }

    @SuppressLint("CommitPrefEdits")
    override fun initUI() {
        radioGroup = findViewById(R.id.ages_radio_group)
        toWebViewButton = findViewById(R.id.to_web_view_button)
        intent1 = Intent(this, WebViewActivity::class.java)
        mSettings = getSharedPreferences(APP_REFERENCES, Context.MODE_PRIVATE)
        mEditor = mSettings.edit()
    }

    override fun getContentView(): Int = R.layout.activity_choose_age

}
