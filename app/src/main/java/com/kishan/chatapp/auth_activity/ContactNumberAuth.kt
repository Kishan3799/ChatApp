package com.kishan.chatapp.auth_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kishan.chatapp.MainActivity
import com.kishan.chatapp.databinding.ActivityContactNumberAuthBinding

//this Activity authenticate the New User in App
class ContactNumberAuth : AppCompatActivity() {
    //Initialize binding for bind the layout in contact activity
    private lateinit var binding: ActivityContactNumberAuthBinding
    //Initializing FirebaseAuth for authentication
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactNumberAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize the FirebaseAuth instance.
        auth = FirebaseAuth.getInstance()

//        check to see if the user is currently signed in. then opne the main Activity
        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

//        inintilaize continue button for login
        binding.button.setOnClickListener {
            //check to see if contact number is not field the user to show the toast to fill the contact number
//            else open otp verification activity to send the otp and then  verify the user
            if(binding.contactNo.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter your contact number", Toast.LENGTH_SHORT).show()
            }else {
                val intent = Intent(this, OTPvarifyActivity::class.java)
                intent.putExtra("number", binding.contactNo.text!!.toString())
                startActivity(intent)
            }
        }

    }
}