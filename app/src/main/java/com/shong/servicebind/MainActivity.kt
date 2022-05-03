package com.shong.servicebind

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    var myService: MyService? = null
    var isConService = false
    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val b = service as MyService.MyServiceBinder
            myService = b.getService(this@MainActivity)
            isConService = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConService = false
        }
    }

    fun serviceBind() {
        if (isConService) {
            toastShow(this, "Already Bound!",Toast.LENGTH_SHORT)
        }else{
            val intent = Intent(this, MyService::class.java)
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            toastShow(this, "Start Bound!",Toast.LENGTH_SHORT)
        }
    }

    fun serviceUnBind() {
        if (isConService) {
            toastShow(this, "Stop Bind!",Toast.LENGTH_SHORT)
            unbindService(serviceConnection)
            isConService = false
        }else{
            toastShow(this, "Not Yet Connected Service!",Toast.LENGTH_SHORT)
        }
    }

    private var toast: Toast? = null
    fun toastShow(context: Context, message: String, length: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, message, length)
        toast?.show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.exampleTextView)
        findViewById<Button>(R.id.startBindButton).setOnClickListener {
            serviceBind()
        }
        findViewById<Button>(R.id.unBindButton).setOnClickListener {
            serviceUnBind()
        }
        findViewById<Button>(R.id.getExampleButton).setOnClickListener {
            tv.text = myService?.getExampleData() ?: "SerVice is Null!"
        }
    }


}