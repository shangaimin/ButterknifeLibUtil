package com.sgx.kotlin

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    @BindView(R.id.recycleview)
    private lateinit var recyclerView: RecyclerView;

    @BindView(R.id.test_02)
    private lateinit var tv2: TextView
    private lateinit var messageAdapter: MessageAdapter;
    private val COMMAND_RESULT_FORMAT = "resultCode:%s,successMsg:%s,errorMsg:%s\n"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectTool.inject(this@MainActivity)
        val launch = GlobalScope.launch {
            println("协程启动了")
        }
        vibrator = this.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?;
//        Toast.makeText(this@MainActivity, tv1.text.toString(), Toast.LENGTH_LONG).show()
        setAlias();
        recyclerView = findViewById(R.id.recycleview)
        messageAdapter = MessageAdapter(ArrayList<JPushMessages>());
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter
        loadData()
        findViewById<TextView>(R.id.clear_tv).setOnClickListener {
//            var result:Boolean=CMD().run("settings put system screen_off_timeout 2147483647")
//            Log.e("zhixing", "kaishi")
//            Thread(Runnable {
//                var result =
//                    ShellUtils.execCommand(
//                        "settings put system screen_off_timeout 2147483647",
//                        true,
//                        true
//                    )
//                Log.e(
//                    "zhixing",
//                    String.format(
//                        COMMAND_RESULT_FORMAT,
//                        result.result,
//                        result.successMsg,
//                        result.errorMsg
//                    )
//                )
//            }).start()
        }
        findViewById<TextView>(R.id.youhua_tv).setOnClickListener {
            ignoreBatteryOptimization(this)
        }
        findViewById<TextView>(R.id.app_setting).setOnClickListener {
            intent = Intent();
            intent.setClassName(
                "com.ecell.appmanager",
                "com.ecell.appmanager.MainActivity"
            ); // 指定应用包名和activity名
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
//        startDevelopmentActivity()
    }

    private fun loadData() {
        messageAdapter.setjPushMessages(GreenUtils.loadAll())
        messageAdapter.setOnLickListener {
            GreenUtils.clearAllMessage(it)
            loadData()
        }
    }

    @OnClickCommon(R.id.test_01)
    private fun show() {
        TTSUtils.getInstance(this).playText("明月", true);

//        Toast.makeText(this@MainActivity, tv1.text.toString(), Toast.LENGTH_LONG).show()

    }

    @OnClickLongCommon(R.id.test_02)
    private fun show1(): Boolean {
        startDevelopmentActivity()
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

    /**
     * 忽略电池优化
     */
    private fun ignoreBatteryOptimization(activity: Activity) {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        var hasIgnored = false
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.packageName)
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored) {
                //未加入电池优化的白名单 则弹出系统弹窗供用户选择(这个弹窗也是一个页面)
                val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:" + activity.packageName)
                startActivity(intent)
            } else {
                //已加入电池优化的白名单 则进入系统电池优化页面
                val powerUsageIntent = Intent(ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                val resolveInfo = packageManager.resolveActivity(powerUsageIntent, 0)
                //判断系统是否有这个页面
                if (resolveInfo != null) {
                    startActivity(powerUsageIntent)
                }
            }
        }
    }

    /**
     * 打开开发者模式界面
     */
    private fun startDevelopmentActivity() {
        try {
            var intent = Intent(Settings.ACTION_NOTIFICATION_ASSISTANT_SETTINGS);
            startActivity(intent);
        } catch (e: Exception) {
            try {
                var componentName = ComponentName(
                    "com.android.settings",
                    "com.android.settings.DevelopmentSettings"
                );
                var intent = Intent();
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.View");
                startActivity(intent);
            } catch (e1: Exception) {
                try {
                    var intent =
                        Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");//部分小米手机采用这种方式跳转
                    startActivity(intent);
                } catch (e2: Exception) {

                }

            }
        }
    }
}