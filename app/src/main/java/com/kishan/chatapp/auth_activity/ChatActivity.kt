package com.kishan.chatapp.auth_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kishan.chatapp.adapter.MessageAdapter
import com.kishan.chatapp.databinding.ActivityChatBinding
import com.kishan.chatapp.modal.MessageModal
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    //sender and receiver user id
    private lateinit var senderUid :String
    private lateinit var receiverUid :String

    //creating room for chat sender and receiver
    private lateinit var senderRoom :String
    private lateinit var receiverRoom :String

    //crating message array List using messageModal
    private lateinit var list : ArrayList<MessageModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initilaize user id to sender and receiver for match the both user id for chat
        senderUid = FirebaseAuth.getInstance().uid.toString()
        receiverUid = intent.getStringExtra("uid")!!

        //initialize the room for sender and receiver useing user id and concatinating the user id
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid

        list = ArrayList()

        database = FirebaseDatabase.getInstance()

        binding.sendMessage.setOnClickListener {
            if (binding.messageBox.text.isEmpty()){
                Toast.makeText(this, "Enter Your message", Toast.LENGTH_SHORT).show()
            }else {
                val message = MessageModal(binding.messageBox.text.toString(), senderUid, Date().time)

                val randomKey = database.reference.push().key

                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!).setValue(message)
                    .addOnSuccessListener {
                        database.reference.child("chats").child(receiverRoom).child("message")
                            .child(randomKey).setValue(message).addOnSuccessListener {
                                binding.messageBox.text = null
                                Toast.makeText(this,"Message send",Toast.LENGTH_SHORT).show()
                            }
                    }
            }

        }

        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(MessageModal::class.java)
                        list.add(data!!)
                    }

                    binding.recyclerView.adapter = MessageAdapter(this@ChatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                }

            })
    }
}