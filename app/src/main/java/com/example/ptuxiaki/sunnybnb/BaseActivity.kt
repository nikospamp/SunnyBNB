package com.example.ptuxiaki.sunnybnb

import android.arch.lifecycle.ProcessLifecycleOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Pampoukidis on 10/1/2018.
 */
abstract class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get()
                .lifecycle
                .addObserver(
                        ForegroundBackgroundListener())
    }

}