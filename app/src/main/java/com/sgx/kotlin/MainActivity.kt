package com.sgx.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.sgx.ioc_annotation_lib.annotation.BindView
import com.sgx.ioc_annotation_lib.annotation.ContentView
import com.sgx.ioc_annotation_lib.InjectTool
import com.sgx.ioc_annotation_lib.annation_common.OnClickCommon
import com.sgx.ioc_annotation_lib.annation_common.OnClickLongCommon
import com.sgx.ioc_annotation_lib.annotation.OnClick
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ContentView(R.layout.activity_main)
class MainActivity : AppCompatActivity() {
    @BindView(R.id.test_01)
    private lateinit var tv1: TextView

    @BindView(R.id.test_02)
    private lateinit var tv2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectTool.inject(this@MainActivity)
        GlobalScope.launch {
            println("协程启动了")
        }
        Toast.makeText(this@MainActivity, tv1.text.toString(), Toast.LENGTH_LONG).show()
    }

    @OnClickCommon(R.id.test_01)
    private fun show() {
        Toast.makeText(this@MainActivity, tv1.text.toString(), Toast.LENGTH_LONG).show()

    }

    @OnClickLongCommon(R.id.test_02)
    private fun show1():Boolean {
        Toast.makeText(this@MainActivity, tv2.text.toString(), Toast.LENGTH_LONG).show()
        return false;
    }
}