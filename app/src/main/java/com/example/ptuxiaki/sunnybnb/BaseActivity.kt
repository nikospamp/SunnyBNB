package com.example.ptuxiaki.sunnybnb

import android.arch.lifecycle.ProcessLifecycleOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Pampoukidis on 10/1/2018.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mAuth.currentUser != null)
            ProcessLifecycleOwner.get()
                    .lifecycle
                    .addObserver(
                            ForegroundBackgroundListener())
    }

}