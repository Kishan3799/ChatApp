package com.kishan.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.kishan.chatapp.adapter.ViewPagerAdapter
import com.kishan.chatapp.auth_activity.ContactNumberAuth
import com.kishan.chatapp.databinding.ActivityMainBinding
import com.kishan.chatapp.ui.CallFragment
import com.kishan.chatapp.ui.ChatFragment
import com.kishan.chatapp.ui.StatusFragment

class MainActivity : AppCompatActivity() {
    //Intialize binding for bind the layout in main activity
    private lateinit var binding: ActivityMainBinding
    //Intializing FirebaseAutj for authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        creating fragment arraylist to intilize all fragments
        val fragmentArrayList = ArrayList<Fragment>()

//        adding Fragment in fragment arraylist
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        // initialize the FirebaseAuth instance.
        auth = FirebaseAuth.getInstance()

        //checking to see current user is sign in or not. If user is not sign in then open authentication activity
        if (auth.currentUser == null){
            startActivity(Intent(this, ContactNumberAuth::class.java))
            finish()
        }


//        initialize adapter to built in ViewPagerAdapter
        val adapter = ViewPagerAdapter(this, supportFragmentManager, fragmentArrayList)

//        initialize adapter in viewpager main xml layout
        binding.viewPager.adapter = adapter

        // and the initialize tabs to viewpager
        binding.tabs.setupWithViewPager(binding.viewPager)

    }
}