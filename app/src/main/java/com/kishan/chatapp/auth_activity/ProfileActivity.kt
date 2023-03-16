package com.kishan.chatapp.auth_activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kishan.chatapp.MainActivity
import com.kishan.chatapp.R
import com.kishan.chatapp.databinding.ActivityProfileBinding
import com.kishan.chatapp.modal.UserModal
import java.util.*

/**
 * this activity is used for creating user profile
 */

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    // creating FirebaseDatabase variable for RealTime Database
    private lateinit var database:FirebaseDatabase
    // creating FirebaseStorage variable for storage
    private lateinit var storag: FirebaseStorage
    // creating selectImage variable  for image url
    private lateinit var selectImage:Uri
    private lateinit var dialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this)
            .setMessage("Updating Profile..")
            .setCancelable(false)

        /**
         * initialize the FirebaseDatabase, FirebaseStorage , FirebaseAuth instance.
         */
        database = FirebaseDatabase.getInstance()
        storag = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        //userImage Button
        binding.userImage.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        //Continue button
        binding.continueBtn.setOnClickListener{
            if(binding.userName.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your name", Toast.LENGTH_SHORT).show()
            }else if(selectImage == null){
                Toast.makeText(this, "Please select your image", Toast.LENGTH_SHORT).show()
            }
            else{
                uploadData()
            }
        }
    }

    /**
     * This function is upload image on the firebase storage
     */
    private fun uploadData() {
        val reference = storag.reference.child("Profile").child(Date().time.toString())
        // Register observers to listen for when the download is done or if it fails
        reference.putFile(selectImage).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user = UserModal(auth.uid.toString(), binding.userName.text.toString(), auth.currentUser!!.phoneNumber.toString(),imgUrl)
        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){
            if(data.data != null){
                selectImage = data.data!!
                binding.userImage.setImageURI(selectImage)
            }
        }
    }
}