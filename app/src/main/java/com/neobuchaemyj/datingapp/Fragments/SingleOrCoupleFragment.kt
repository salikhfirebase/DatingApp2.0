package com.neobuchaemyj.datingapp.Fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Model.User
import com.neobuchaemyj.datingapp.QuestionnaireActivity

import com.neobuchaemyj.datingapp.R
import com.neobuchaemyj.datingapp.UserProfileActivity
import io.reactivex.Completable
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
class SingleOrCoupleFragment : Fragment() {

    private var fragmentMain = androidx.fragment.app.Fragment()
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var checkedHaveHouse: TextView
    lateinit var checkedHaveCar: TextView
    lateinit var checkedHaveMoto: TextView
    lateinit var makeClear: EditText
    lateinit var agreeCheckBox: CheckBox
    lateinit var nextButton: Button
    lateinit var checkedRadioText: String
    lateinit var db: AppDatabase
    var otherData = ""
    var houseCheked = ""
    var carCheked = ""
    var motoCheked = ""
    var userId = 0
    var user = User()
    lateinit var intent1: Intent

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_or_couple, container, false)

        radioGroup = view.findViewById(R.id.radioGroup)
        checkedHaveHouse = view.findViewById(R.id.have_house)
        checkedHaveCar = view.findViewById(R.id.have_car)
        checkedHaveMoto = view.findViewById(R.id.have_motorcycle)
        makeClear = view.findViewById(R.id.make_clear_edit_text)
        agreeCheckBox = view.findViewById(R.id.agree_checkbox)
        nextButton = view.findViewById(R.id.second_next_button)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase

        Observable.fromCallable { db.userDao().getLastId() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userId = it
            }, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })

        nextButton.setOnClickListener {
            radioButton = view.findViewById(radioGroup.checkedRadioButtonId)
            checkedRadioText = radioButton.text.toString()

            checkedHaveHouse.setOnClickListener {
                houseCheked = if (houseCheked == "") {
                    checkedHaveHouse.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveHouse.text.toString() + ";"
                } else {
                    checkedHaveHouse.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }
            checkedHaveCar.setOnClickListener {
                carCheked = if (carCheked == "") {
                    checkedHaveCar.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveCar.text.toString() + ";"
                } else {
                    checkedHaveCar.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }
            checkedHaveMoto.setOnClickListener {
                motoCheked = if (motoCheked == "") {
                    checkedHaveMoto.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveMoto.text.toString() + ";"
                } else {
                    checkedHaveMoto.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }
            userSet()
            if (userId != 0) {
                saveToDb()
                intent1 = Intent(this.requireContext(), UserProfileActivity::class.java)
                startActivity(intent1)
            } else {
                Toast.makeText(this.requireContext(), "Wait", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

    @SuppressLint("CheckResult")
    fun userSet() {
        user.setOtherData(otherData)
        user.setStatus(checkedRadioText)
        user.setMakeClear(makeClear.text.toString())
        if (agreeCheckBox.isChecked) {
            user.setAgreement("true")
        } else {
            user.setAgreement("false")
        }
    }

    @SuppressLint("CheckResult")
    fun saveToDb() {
        Completable.fromAction { db.userDao().updateotherData(user.getOtherData(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateStatus(user.getStatus(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateMakeClear(user.getMakeClear(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateAgreement(user.getAgreement(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
    }


}
