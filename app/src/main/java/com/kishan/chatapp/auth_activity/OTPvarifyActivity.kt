package com.kishan.chatapp.auth_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.kishan.chatapp.databinding.ActivityOtpvarifyBinding
import java.util.concurrent.TimeUnit

/**
 * This is verify the user to chat app*/
class OTPvarifyActivity : AppCompatActivity() {
    //creating binding variable for bind the layout in contact activity
    private lateinit var binding : ActivityOtpvarifyBinding
    // creating FirebaseAuth variable for authentication
    private lateinit var auth:FirebaseAuth
    // createing verificationId variable for verifying
    private lateinit var verificationId : String
    //creating alert variable for showing alert dialog builder
    private lateinit var alert:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpvarifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize the FirebaseAuth instance.
        auth = FirebaseAuth.getInstance()

        //creating builder variable and initialize AlertDialog builder
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please wait...")
        builder.setTitle("Loading")
        builder.setCancelable(false)
        alert = builder.create()
        alert.show()

        //creating contactNumber variable to initialize user contact number
        val contactNumber = "+91"+intent.getStringExtra("number")
        //
        val options = PhoneAuthOptions.Builder(auth)
            .setPhoneNumber(contactNumber) //phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) //Timeout and unit
            .setActivity(this) //Activity(for call backs)
                // OnVerificationStateChangedCallbacks
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    TODO("Not yet implemented")
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    alert.dismiss()
                   Toast.makeText(this@OTPvarifyActivity, "Please try Again ${p0}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(id, token)
                    alert.dismiss()
                    verificationId = id
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        //initilize layout otp button
        binding.OtpEnterBtn.setOnClickListener{
            if(binding.OtpNo.text!!.isEmpty()){
                Toast.makeText(this, "Please enter Otp", Toast.LENGTH_SHORT).show()
            }else{
                alert.show()

                val credential = PhoneAuthProvider.getCredential(verificationId, binding.OtpNo.text!!.toString())

                auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            alert.dismiss()
                            startActivity(Intent(this,ProfileActivity::class.java))
                            finish()
                        }else{
                            alert.dismiss()
                            Toast.makeText(this,"Error ${it.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }
}