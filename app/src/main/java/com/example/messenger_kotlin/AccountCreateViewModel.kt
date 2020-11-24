package com.example.messenger_kotlin

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        if(email.isEmpty()) {
            val show = AlertDialog.Builder(AccountCreateActivity())
                .setTitle("ERROR！")
                .setMessage("パス入力してください")
                .setPositiveButton("OK") { dialog, which -> }
                .show()
            return
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches() == false){
            val show = AlertDialog.Builder(AccountCreateActivity())
                .setTitle("ERROR！")
                .setMessage("パスワードが適切じゃない")
                .setPositiveButton("OK") { dialog, which -> }
                .show()
            return
        }

        if(password.isEmpty()) {
            val show = AlertDialog.Builder(AccountCreateActivity())
                .setTitle("ERROR！")
                .setMessage("入力してください")
                .setPositiveButton("OK") { dialog, which -> }
                .show()
            return
        }
        else if(password.length < 6) {
            val show = AlertDialog.Builder(AccountCreateActivity())
                .setTitle("ERROR！")
                .setMessage("足りねえぜ")
                .setPositiveButton("OK") { dialog, which -> }
                .show()
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

        //Firebase:passwordとemailでユーザ作成
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() {
                if (!it.isSuccessful) {
                    uploadImageToFirebase(selectedPhotoUri)
                    return@addOnCompleteListener
                }
            }

            .addOnFailureListener {
                    return@addOnFailureListener
            }
    }

}
