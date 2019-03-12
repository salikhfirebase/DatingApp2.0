package com.neobuchaemyj.datingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Fragments.MainInfoFragment
import com.neobuchaemyj.datingapp.Fragments.RegistrationFragment

class QuestionnaireActivity : AppCompatActivity() {

    private var fragmentMain = androidx.fragment.app.Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        if (intent.getStringExtra("action") == "registration") {
            fragmentMain = RegistrationFragment()
        }
        setFragment(fragmentMain)
    }

    fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.question_container, f)
        ft.commit()

    }
}
