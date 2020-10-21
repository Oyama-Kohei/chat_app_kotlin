package com.example.messenger_kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_accountcreate.*
import java.util.*


class AccountCreateFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_accountcreate, container, false)

        button_CreateAccount.setOnClickListener {
            performAccountCreate()
        }

        button_Register_photo.setOnClickListener {

                //画像を取得するピッカをIntentで起動
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 0)
            }

            text_already.setOnClickListener {
                findNavController().navigate(R.id.nav_login)
            }
            return view
        }

//    onActivityResultメソッドには、startActivityForResultの引数としたリクエストコードと、遷移後のActivity からの結果として 結果コード と データ の２つの値を受け取ります。
//    結果コードは RESULT_OK か RESULT_CANCELED のどちらかで、結果を返す側の Activity にて適切な値が返るようにコーディングする必要があります。
//    データは結果コード以外の任意の値を受け取る為の物で、こちらも結果を返す側の Activity で値をセットします。で値をセットします

        var selectedPhotoUri: Uri? = null
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
                selectedPhotoUri = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
                val bitmapDrawable = BitmapDrawable(bitmap)
                button_Register_photo.setBackgroundDrawable(bitmapDrawable)
            }
        }
    }

    private fun performAccountCreate() {
        val email = editText_Email_CreateAccount.editText_Email_CreateAccount.getText().toString()
        val password = editText_Password_CreateAccount.text.toString()

        if (email.isEmpty()) {
            //画面中央に表示してすぐに消えるやつ
            Toast.makeText(this, "メールアドレスを入力してください", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            //画面中央に表示してすぐに消えるやつ
            Toast.makeText(this, "パスワードを入力してください", Toast.LENGTH_SHORT).show()
            return
        }

        //Firebase:passwordとemailでユーザ作成
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    uploadImageToFirebase()
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "アカウント作成に失敗しました", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebase() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Success upload image")
            }
    }
}


