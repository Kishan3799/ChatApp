package com.kishan.chatapp.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kishan.chatapp.R
import com.kishan.chatapp.databinding.FragmentStatusBinding
import com.kishan.chatapp.modal.UserModal
import java.io.File

class StatusFragment : Fragment() {

    private lateinit var binding: FragmentStatusBinding
    private lateinit var database : DatabaseReference
    private lateinit var auth :FirebaseAuth
    private lateinit var storage : StorageReference
    private lateinit var user : UserModal
    private lateinit var userId :String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStatusBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().getReference("users")
        if(userId.isNotEmpty()){
            getUserBio()
        }


        return binding.root
    }

    private fun getUserBio() {
        database.child(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(UserModal::class.java)!!
                getUserImage()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , "Failed to Load", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUserImage(){
       storage = FirebaseStorage.getInstance().getReference("Profile/1678860567499")
        val localeFile = File.createTempFile("tempFile", ".jpg")
        storage.getFile(localeFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localeFile.absolutePath)
            binding.userImage.setImageBitmap(bitmap)
        }.addOnFailureListener{
            Toast.makeText(context , "Failed to Load", Toast.LENGTH_SHORT).show()
        }
    }


}