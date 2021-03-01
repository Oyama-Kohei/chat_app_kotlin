package com.example.messenger_kotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_account_create.*
import java.util.*

class AccountCreateActivity : AppCompatActivity() {
    //companion：static変数、メソッドの代わり
    companion object {
        val TAG = "AccountCreateActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_create)

        button_create_account.setOnClickListener {
            performRegister()
        }

        already_have_account_test_view.setOnClickListener {
            Log.d("AccountCreateActivity", "Try to show Login Activity")

            //::class.javaでクラスオブジェクトの参照
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener {
            Log.d("AccountCreateActivity", "Try to show photo selector")
            //ユーザーが連絡先を選択し、アプリが連絡先情報にアクセスできるようにする
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    //varの場合、変更が可能
    var selectedPhotoUri: Uri? = null

    //画像取得の終了後に来る処理
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("AccountCreateActivity", "Photo was selected")

            //Bitmapで画像を取得、フォームの周りに黒線もひいとく
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectphoto_imageview_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f

//            val bitmapDrawable = BitmapDrawable(bitmap)
//            selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
            //selectphoto_button_register.alpha = 0f

        }
    }

    //Registerボタン押下！
    private fun performRegister() {
        val email = editText_Email_CreateAccount.text.toString()
        val password = editText_Password_CreateAccount.text.toString()

        //emailとpwの入力チェック
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("AccountCreateActivity", "Email is: " + email)
        Log.d("AccountCreateActivity", "Password: $password")

        //サインアップ
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if Successful
                Log.d("AccountCreateActivity", "Successfully created user with uid: ${it.result?.user?.uid}")
                uploadImageToFireBaseStorage()
            }
            .addOnFailureListener {
                Log.d("AccountCreateActivity", "Failed to create User: ${it.message}")
                Toast.makeText(this, "Failed to create User: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    //ストレージへのプロフィール写真の保存、ユーザー情報をDBへ保存
    //images配下に採番されたファイルIDで登録を行う
    private fun uploadImageToFireBaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("AccountCreateActivity", "Successfuly uploaded image:${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener { uri ->
                    Log.d("AccountCreateActivity", "File Location :$uri")

                    //画像の保存が完了したタイミングでDBへの登録も行う
                    saveUserToFirebaseDatabase(uri.toString())
                }
            }
            .addOnFailureListener {
                //しくじった時、考えないようにする
            }
    }

    //Realtime Databaseにデータを格納
    //users配下にユーザーID単位で登録する
    private fun saveUserToFirebaseDatabase(profileImageUri: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, editText_Username_CreateAccount.text.toString(), profileImageUri)

        //Intent.FLAG_ACTIVITY_CLEAR_TASK:Activityのスタックを削除して起動する
        //Intent.FLAG_ACTIVITY_NEW_TASK:タスクがスタックに存在しても新しいタスクとして起動

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("AccountCreateActivity", "Finally we saved the user to Firebase Database")
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d("AccountCreateActivity", "Failed to set value to Database")
            }
    }
}





