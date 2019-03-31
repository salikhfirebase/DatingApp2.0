package com.neobuchaemyj.datingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EditUserInfoActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    var userId = 0
    var userEmail = ""
    var user = User()
    var otherData = ""
    var houseCheked = ""
    var carCheked = ""
    var motoCheked = ""
    lateinit var userNick: EditText
    lateinit var userMakeClear: EditText
    lateinit var years_spinner: Spinner
    lateinit var days_spinner: Spinner
    lateinit var months_spinner: Spinner
    lateinit var race_spinner: Spinner
    lateinit var height_seek: SeekBar
    lateinit var weight_seek: SeekBar
    lateinit var height_text: TextView
    lateinit var weight_text: TextView
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var checkedHaveHouse: TextView
    lateinit var checkedHaveCar: TextView
    lateinit var checkedHaveMoto: TextView
    lateinit var saveButton: Button
    lateinit var checkedRadioText: String
    lateinit var intent1: Intent

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_info)

        userId = intent.getIntExtra("id", 0)
        userEmail = intent.getStringExtra("email")
        db = AppDatabase.getInstance(this) as AppDatabase
        userNick = findViewById(R.id.edit_nick_edit_text)
        userMakeClear = findViewById(R.id.edit_make_clear_edit_text)
        years_spinner = findViewById(R.id.edit_years_spinner)
        days_spinner = findViewById(R.id.edit_days_spinner)
        months_spinner = findViewById(R.id.edit_months_spinner)
        race_spinner = findViewById(R.id.edit_race_spinner)
        height_seek = findViewById(R.id.edit_height_seek_bar)
        weight_seek = findViewById(R.id.edit_weight_seek_bar)
        height_text = findViewById(R.id.edit_textView5)
        weight_text = findViewById(R.id.edit_textView8)
        saveButton = findViewById(R.id.edit_second_next_button)
        radioGroup = findViewById(R.id.edit_radioGroup)
        checkedHaveHouse = findViewById(R.id.edit_have_house_text_view)
        checkedHaveCar = findViewById(R.id.edit_have_car_text_view)
        checkedHaveMoto = findViewById(R.id.edit_have_motorcycle_text_view)

        if (userId != 0) {
            Observable.fromCallable{ db.userDao().getUserFromDbById(userId) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    user = it
                    setViews()
                }
        }

        if (userEmail != "") {
            Observable.fromCallable{ db.userDao().getUserFromDb(userEmail) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    user = it
                    setViews()
                }
        }

        checkedHaveHouse.setOnClickListener {
            houseCheked = if (houseCheked == "") {
                it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                checkedHaveHouse.text.toString() + ";"
            } else {
                it.setBackgroundColor(Color.parseColor("#FFFFFF"))
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
                it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                checkedHaveMoto.text.toString() + ";"
            } else {
                it.setBackgroundColor(Color.parseColor("#FFFFFF"))
                ""
            }
        }

        saveButton.setOnClickListener {

            user.setRace(race_spinner.selectedItem.toString())
            user.setHeight(height_text.text.toString())
            user.setWeight(weight_text.text.toString())
            user.setNick(userNick.text.toString())
            user.setMakeClear(userMakeClear.text.toString())
            var birthDate = days_spinner.selectedItem.toString() + "/" + months_spinner.selectedItem.toString() + "/" + years_spinner.selectedItem.toString()
            user.setBirth(birthDate)
            radioButton = findViewById(radioGroup.checkedRadioButtonId)
            checkedRadioText = radioButton.text.toString()
            otherData = houseCheked + motoCheked + carCheked
            user.setOtherData(otherData)
            user.setStatus(checkedRadioText)

            Completable.fromAction { db.userDao().update(user) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this, "Информация обновлена!", Toast.LENGTH_SHORT).show()
                    intent1 = Intent(this, UserProfileActivity::class.java)
                    intent1.putExtra("reg", user.getId())
                    intent1.putExtra("log_in", "")
                    startActivity(intent1)
                }, {
                    Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
                })
        }


    }


    fun setViews() {
        userNick.setText(user.getNick(), TextView.BufferType.EDITABLE)
        userMakeClear.setText(user.getMakeClear(), TextView.BufferType.EDITABLE)
        race_spinner.setSelection(getindex(race_spinner, user.getRace()))
        weight_text.text = user.getWeight()
        height_text.text = user.getHeight()
        if (user.getWeight() != "") {weight_seek.progress = user.getWeight().toInt()}
        if (user.getHeight() != "") {height_seek.progress = user.getHeight().toInt()}
        weight_seek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                weight_text.text = seekBar.progress.toString()
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
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                height_text.text = (seekBar.progress + 140).toString()
            }

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                height_text.text = (seekBar.progress + 140).toString()
            }

        })
    }

    fun getindex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value){
                return i
            }
        }
        return 0
    }

}
