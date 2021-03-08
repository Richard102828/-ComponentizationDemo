package com.example.componentizationdemo

import android.app.Application
import com.example.utils.ActivityStackManager

/**
 * @author Wenhao Zhang
 * @date on 2020.12.24
 * @describe application
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityStackManager.ActivityLifecycleCallbacks)
    }
}