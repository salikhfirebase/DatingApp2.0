package com.neobuchaemyj.datingapp.Fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Model.User
import com.neobuchaemyj.datingapp.R
import com.neobuchaemyj.datingapp.TakePictureActivity
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
class LastRegFragment : Fragment() {

    lateinit var checkedRadioText: String
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var db: AppDatabase
    lateinit var nextButton: Button
    lateinit var agreeCheckBox: CheckBox
    var user = User()
    var userId = 0
    lateinit var intent1: Intent

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_last_reg, container, false)

        radioGroup = view.findViewById(R.id.radioGroupLasPage)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase
        nextButton = view.findViewById(R.id.second_next_button2)
        agreeCheckBox = view.findViewById(R.id.agree_checkbox)


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
            user.setLookFor(checkedRadioText)
            if (agreeCheckBox.isChecked) {
                Completable.fromAction { db.userDao().updateLookFor(user.getLookFor(), userId) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}, {
                        Log.d("SaveToDb", "Didn't saved in Last Fragment", it)
                    })
                intent1 = Intent(this.requireContext(), TakePictureActivity::class.java)
                startActivity(intent1)

            } else {
                Toast.makeText(this.requireContext(), "Примите правила пользования", Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }


}
