package com.example.messenger_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object{
    val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        if (user != null) {
            supportActionBar?.title = user.username
        }

        setupDummyData()
        button_send_chat.setOnClickListener{
            Log.d(TAG, "Attemp to send message")
            performSendMessage()
        }
    }

    private fun performSendMessage(){

        val text = editText_chat_log.text.toString()

        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage = ChatMessage()
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "sendボタン")
            }
    }

    private fun setupDummyData(){
        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ChatFromItem("tosfkjbf"))
        adapter.add(ChatToItem("tosfkjbf"))
        adapter.add(ChatFromItem("tosfkjbf"))

        recyclerview_chat_log.adapter = adapter
    }
}
class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long){

}

class ChatFromItem(val text:String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = text
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text:String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}