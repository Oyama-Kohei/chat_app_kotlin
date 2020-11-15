package com.example.messenger_kotlin

class Validaion {
    fun password_check(password: String): String {

        val Messege: String

        if (password.length < 6) {
            Messege = "パスワードは6桁以上で入力してください"
            return Messege
        }
        else{
            return Messege
        }
    }

}