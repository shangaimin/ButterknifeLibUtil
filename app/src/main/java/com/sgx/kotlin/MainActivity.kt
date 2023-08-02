package com.sgx.kotlin

import android.app.Service
import android.media.AudioAttributes
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.sgx.ioc_annotation_lib.annotation.BindView
import com.sgx.ioc_annotation_lib.annotation.ContentView
import com.sgx.ioc_annotation_lib.InjectTool
import com.sgx.ioc_annotation_lib.annation_common.OnClickCommon
import com.sgx.ioc_annotation_lib.annation_common.OnClickLongCommon
import com.sgx.ioc_annotation_lib.annotation.OnClick
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import cn.jpush.android.api.JPushInterface

import cn.jpush.android.api.TagAliasCallback


@ContentView(R.layout.activity_main)
class MainActivity : AppCompatActivity() {
    @BindView(R.id.test_01)
    private lateinit var tv1: TextView
    private var vibrator: Vibrator? = null

    @BindView(R.id.test_02)
    private lateinit var tv2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectTool.inject(this@MainActivity)
        val launch = GlobalScope.launch {
            println("协程启动了")
        }
        vibrator = this.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?;
//        Toast.makeText(this@MainActivity, tv1.text.toString(), Toast.LENGTH_LONG).show()
        setAlias();
    }

    @OnClickCommon(R.id.test_01)
    private fun show() {
        TTSUtils.getInstance(this).playText("明月");

//        Toast.makeText(this@MainActivity, tv1.text.toString(), Toast.LENGTH_LONG).show()

    }

    @OnClickLongCommon(R.id.test_02)
    private fun show1(): Boolean {

        Toast.makeText(this@MainActivity, tv2.text.toString(), Toast.LENGTH_LONG).show()
        return false;
    }

    private fun setAlias() {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, "test"))
    }

    private val mAliasCallback =
        TagAliasCallback { code, alias, tags ->
            val logs: String
            when (code) {
                0 -> {
                    logs = "Set tag and alias success"
                    Log.i("TAG", logs)
                }
                6002 -> {
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s."
                    Log.i("TAG", logs)
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(
                        mHandler.obtainMessage(MSG_SET_ALIAS, alias),
                        1000 * 60
                    )
                }
                else -> {
                    logs = "Failed with errorCode = $code"
                    Log.e("TAG", logs)
                }
            }
        }
    private val MSG_SET_ALIAS = 1001
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_SET_ALIAS -> {
                    Log.d("TAG", "Set alias in handler.")
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(
                        applicationContext,
                        msg.obj as String,
                        null,
                        mAliasCallback
                    )
                }
                else -> Log.i("TAG", "Unhandled msg - " + msg.what)
            }
        }
    }
}