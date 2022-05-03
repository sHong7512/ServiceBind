package com.shong.servicebind

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService : Service() {
    val TAG = "${this::class.java.simpleName}_sHong"
    private var boundActivity: MainActivity? = null
    private val binder = MyServiceBinder()
    inner class MyServiceBinder : Binder() {
        fun getService(activity: MainActivity): MyService {
            this@MyService.boundActivity = activity
            return this@MyService
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind")
        return binder
    }

    private var job: Job? = null
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            delay(5000L)
            destroyService()
        }
    }

    private fun destroyService(){
        try{
            Log.d(TAG, "destroyService")
            boundActivity?.serviceUnBind()
            stopSelf()
        }catch (e: Exception){
            Log.d(TAG, "$e")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        job?.cancel()
    }

    fun getExampleData() = "Example Data Get Completed!!"
}