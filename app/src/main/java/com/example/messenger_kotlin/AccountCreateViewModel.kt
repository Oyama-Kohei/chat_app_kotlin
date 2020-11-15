package com.example.messenger_kotlin

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AccountCreateViewModel (
//    private val accountDataRepository: AccountDataRepository
) : ViewModel(){
    fun updateCustomer(
        selectedPhotoUri: Uri?,
        username: String,
        email: String,
        password: String
    ){
        if (email.isEmpty()) {
            return
        }
        if (password.isEmpty()) {
            return
        }
        else {
            performAccountCreate(selectedPhotoUri, username, email, password)
        }
    }

    private fun uploadImageToFirebase(selectedPhotoUri: Uri?) {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Success upload image")
            }
    }

    private fun performAccountCreate(
        selectedPhotoUri: Uri?,
        username: String,
        email: String,
        password: String) {

        if (email.isEmpty()) {
            return
        }

        if (password.isEmpty()) {
            //画面中央に表示してすぐに消えるやつ
//            Toast.makeText(context, "パスワードを入力してください", Toast.LENGTH_SHORT).show()
            return
        }

        //Firebase:passwordとemailでユーザ作成
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() {
                if (!it.isSuccessful) {
                    uploadImageToFirebase(selectedPhotoUri)
                    return@addOnCompleteListener
                }
            }

            .addOnFailureListener {
//                Toast.makeText(context, "アカウント作成に失敗しました", Toast.LENGTH_SHORT).show()
            }
    }

}

/*Androidアーキテクチャコンポーネントのビューモデルの場合、
メモリリークとして、アクティビティコンテキストをアクティビティのViewModelに渡すことはお勧めできません。
したがって、ViewModelでコンテキストを取得するには、ViewModelクラスでAndroid ViewModelクラスを拡張する
必要があります。このようにして、以下のサンプルコードに示すようなコンテキストを取得できます。
 */