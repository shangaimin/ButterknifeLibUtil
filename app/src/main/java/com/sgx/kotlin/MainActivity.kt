package com.sgx.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.sgx.ioc_annotation_lib.BindView
import com.sgx.ioc_annotation_lib.ContentView
import com.sgx.ioc_annotation_lib.InjectTool
import com.sgx.ioc_annotation_lib.OnClick
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ContentView(R.layout.activity_main)
class MainActivity : AppCompatActivity() {
    @BindView(R.id.test_01)
    private lateinit var tv1: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectTool.inject(this@MainActivity)
        GlobalScope.launch {
            println("协程启动了")
        }
        Toast.makeText(this@MainActivity,tv1.text.toString(),Toast.LENGTH_LONG).show()
    }
    @OnClick(R.id.test_01)
    private fun show() {
        Toast.makeText(this@MainActivity,tv1.text.toString(),Toast.LENGTH_LONG).show()

    }
}