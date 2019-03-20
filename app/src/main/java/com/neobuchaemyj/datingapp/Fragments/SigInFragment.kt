package com.neobuchaemyj.datingapp.Fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.neobuchaemyj.datingapp.DB.AppDatabase

import com.neobuchaemyj.datingapp.R
import com.neobuchaemyj.datingapp.TakePictureActivity
import com.neobuchaemyj.datingapp.UserProfileActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SigInFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var email_edit_text: EditText
    private lateinit var pass_edit_text: EditText
    private lateinit var log_in_button: Button
    lateinit var intent1: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sig_in, container, false)

        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase
        email_edit_text = view.findViewById(R.id.sign_in_email_edit_text)
        pass_edit_text = view.findViewById(R.id.sign_in_pass_edit_text)
        log_in_button = view.findViewById(R.id.sign_in_button)

        log_in_button.setOnClickListener {
            isUserInDb(email_edit_text.text.toString(), pass_edit_text.text.toString())
        }

        return view
    }

    @SuppressLint("CheckResult")
    fun isUserInDb(email: String, password: String) {

        Observable.fromCallable{ db.userDao().verifaicate(email,password) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it>0) {
                    intent1 = Intent(this.requireContext(), UserProfileActivity::class.java)
                    intent1.putExtra("log_in", email)
                    intent1.putExtra("reg", 0)
                    startActivity(intent1)
                } else {
                    Toast.makeText(this.requireContext(), "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }

    }




}
