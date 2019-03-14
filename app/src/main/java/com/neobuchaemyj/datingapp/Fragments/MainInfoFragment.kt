package com.neobuchaemyj.datingapp.Fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Model.User

import com.neobuchaemyj.datingapp.R
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
class MainInfoFragment : Fragment() {

    private var fragmentMain = androidx.fragment.app.Fragment()
    lateinit var nick_edit: EditText
    lateinit var years_spinner: Spinner
    lateinit var days_spinner: Spinner
    lateinit var months_spinner: Spinner
    lateinit var race_spinner: Spinner
    lateinit var height_seek: SeekBar
    lateinit var weight_seek: SeekBar
    lateinit var height_text: TextView
    lateinit var weight_text: TextView
    lateinit var nextButton: Button
    lateinit var db: AppDatabase

    var user = User()
    var birthDate = ""
    var userHeight = ""
    var userWeight = ""
    var userId = 0


    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_main_info, container, false)

        nick_edit = view.findViewById(R.id.nick_edit_text)
        years_spinner = view.findViewById(R.id.years_spinner)
        days_spinner = view.findViewById(R.id.days_spinner)
        months_spinner = view.findViewById(R.id.months_spinner)
        race_spinner = view.findViewById(R.id.race_spinner)
        height_seek = view.findViewById(R.id.height_seek_bar)
        weight_seek = view.findViewById(R.id.weight_seek_bar)
        height_text = view.findViewById(R.id.textView5)
        weight_text = view.findViewById(R.id.textView8)
        nextButton = view.findViewById(R.id.first_next_button)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase
        Observable.fromCallable { db.userDao().getLastId() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userId = it
            }, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })



        weight_seek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                weight_text.text = seekBar.progress.toString()
                userWeight = seekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                weight_text.text = seekBar.progress.toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                weight_text.text = seekBar.progress.toString()
            }

        })

        height_seek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                height_text.text = (seekBar.progress + 140).toString()
                userHeight = (seekBar.progress + 140).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                height_text.text = (seekBar.progress + 140).toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                height_text.text = (seekBar.progress + 140).toString()
            }

        })

        nextButton.setOnClickListener {
            userSet()
            if (userId != 0) {
                saveToDb()
                fragmentMain = SingleOrCoupleFragment()
                setFragment(fragmentMain)
            } else {
                Toast.makeText(this.requireContext(), "Wait", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    @SuppressLint("CheckResult")
    fun userSet() {
        birthDate = days_spinner.selectedItem.toString() + "/" + months_spinner.selectedItem.toString() + "/" + years_spinner.selectedItem.toString()
        user.setNick(nick_edit.text.toString())
        user.setBirth(birthDate)
        user.setRace(race_spinner.selectedItem.toString())
        user.setHeight(userHeight)
        user.setWeight(userWeight)

    }

    @SuppressLint("CheckResult")
    fun saveToDb() {

        Completable.fromAction { db.userDao().updateNick(user.getNick(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateBirth(user.getBirth(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateRace(user.getRace(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateHeight(user.getHeight(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateWeight(user.getWeight(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
    }

    fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = this.requireActivity().supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.question_container, f)
        ft.commit()

    }



}
