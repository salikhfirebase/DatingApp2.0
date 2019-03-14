package com.neobuchaemyj.datingapp.Fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Model.User

import com.neobuchaemyj.datingapp.R
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_registration.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RegistrationFragment : Fragment() {

    private var fragmentMain = androidx.fragment.app.Fragment()
    private lateinit var db:AppDatabase
    private lateinit var email_edit_text: EditText
    private lateinit var pass_edit_text: EditText
    private var user = User()
    private lateinit var create_acc_button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_registration, container, false)

        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase
        email_edit_text = view.findViewById(R.id.reg_email_edit_text)
        pass_edit_text = view.findViewById(R.id.reg_pass_edit_text)
        create_acc_button = view.findViewById(R.id.reg_button)

        create_acc_button.setOnClickListener {
            isUserInDb(email_edit_text.text.toString(), reg_pass_edit_text.text.toString())
        }

        return view
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
    fun isUserInDb(email: String, password: String) {

        Observable.fromCallable{ db.userDao().isEmailInDb(email) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it>0) {
                    Toast.makeText(this.requireContext(), "That's email already exist", Toast.LENGTH_SHORT).show()
                } else {
                    user.setEmail(email)
                    user.setPassword(password)
                    saveToDb(user)
                    fragmentMain = MainInfoFragment()
                    setFragment(fragmentMain)
                }
            }

    }

    fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = this.requireActivity().supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.question_container, f)
        ft.commit()

    }


}
