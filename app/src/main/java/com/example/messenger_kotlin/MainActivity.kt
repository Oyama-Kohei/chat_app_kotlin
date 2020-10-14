package com.example.messenger_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
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

            if(email.isEmpty()){
                //画面中央に表示してすぐに消えるやつ
                Toast.makeText(this, "メールアドレスを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                //画面中央に表示してすぐに消えるやつ
                Toast.makeText(this, "パスワードを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Firebase:passwordとemailでユーザ作成
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful){
                        return@addOnCompleteListener
                        Log.d("Main", "Success user create")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "アカウント作成に失敗しました", Toast.LENGTH_SHORT).show()
                }

        }

        text_already.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}