package com.kishan.chatapp.ui

import android.os.Bundle
import android.os.UserManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kishan.chatapp.R
import com.kishan.chatapp.adapter.ChatAdapter
import com.kishan.chatapp.databinding.FragmentChatBinding
import com.kishan.chatapp.modal.UserModal


class ChatFragment : Fragment() {

    private lateinit var binding:FragmentChatBinding
    private lateinit var database: FirebaseDatabase
    //creating user list variable by using ArrayList
    private lateinit var userList: ArrayList<UserModal>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)

        database = FirebaseDatabase.getInstance()
        //initialize userList to ArrayList instance
        userList = ArrayList()

        //
        database.reference.child("users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    for (snapshot1 in snapshot.children){
                        val user = snapshot1.getValue(UserModal::class.java)
                        if(user!!.uid != FirebaseAuth.getInstance().uid){
                            userList.add(user)
                        }
                    }

                    binding.userListRecyclerView.adapter = ChatAdapter(requireContext(),userList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        return binding.root
    }

}