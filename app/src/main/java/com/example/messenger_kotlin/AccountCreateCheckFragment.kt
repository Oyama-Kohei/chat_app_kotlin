package com.example.messenger_kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_accountcreate.view.*

class AccountCreateCheckFragment : Fragment() {

    var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accountcreate_check, container, false)


//        text_already.setOnClickListener {
//            findNavController().navigate(R.id.nav_login)
//        }
        return view
    }
}