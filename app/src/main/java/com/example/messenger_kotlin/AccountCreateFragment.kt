package com.example.messenger_kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_accountcreate.*
import kotlinx.android.synthetic.main.fragment_accountcreate.view.*

class AccountCreateFragment : Fragment() {

    private val viewModel: AccountCreateViewModel by viewModels()
    private var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accountcreate, container, false)

        view.button_CreateAccount.setOnClickListener {
            val username = view.editText_Username_CreateAccount?.text.toString()
            val email = view.editText_Email_CreateAccount?.text.toString()
            val password = view.editText_Password_CreateAccount?.text.toString()

           viewModel.updateCustomer(selectedPhotoUri, username, email, password)
        }

        view.button_Register_photo.setOnClickListener {
            //画像を取得するピッカをIntentで起動
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        text_already.setOnClickListener {
//            findNavController().navigate(R.id.nav_login)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(context?.contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            button_Register_photo.setBackgroundDrawable(bitmapDrawable)
        }
    }
}
