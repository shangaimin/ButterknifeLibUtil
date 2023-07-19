package com.sgx.kotlin.lifecycleview

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.sgx.ioc_annotation_lib.annotation.ContentView
import com.sgx.ioc_annotation_lib.InjectTool
import com.sgx.kotlin.R
import java.util.*
@ContentView(R.layout.activity_main)
class SecondActivity : AppCompatActivity() {
    private var myListener:Objects ?=null;
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        InjectTool.inject(this@SecondActivity);
    }
}