package com.example.messenger_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_AccountCreate.setOnClickListener{
            val email = editText_Email.text.toString()
            val password = editText_Password.text.toString()

            //確認用
            Log.d("Main", "Email :" + email)
            Log.d("Main", "Password :" + password)
        }
    }
}