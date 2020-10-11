package com.example.messenger_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.text_already

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_CreateAccount.setOnClickListener{
            val email = editText_Email_CreateAccount.text.toString()
            val password = editText_Password_CreateAccount.text.toString()

            //確認用
            Log.d("Main", "Email :" + email)
            Log.d("Main", "Password :" + password)
        }

        text_already.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}