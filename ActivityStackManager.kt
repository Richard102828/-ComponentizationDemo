package com.example.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Wenhao Zhang
 * @date on 2020.12.24
 * @describe 一个用于管理Activity的栈
 */
object ActivityStackManager {
    private val stack: LinkedList<Activity> by lazy {
        LinkedList<Activity>()
    }

    private val appBackgroundListeners: MutableList<OnAppBackgroundListener> by lazy {
        ArrayList()
    }

    object ActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            registerActivity(activity)
        }


        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {
            appBackgroundListeners.forEach {
                it.onAppForeground()
            }
        }

        override fun onActivityPaused(activity: Activity) {
            TODO("Not yet implemented")
        }

        override fun onActivityStopped(activity: Activity) {
            appBackgroundListeners.forEach {
                it.onAppBackground()
            }
        }


        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            unregisterActivity(activity)
        }
    }

    /**
     * 注册Activity
     */
    fun registerActivity(activity: Activity) {
        synchronized(ActivityStackManager::class.java) {
            stack.remove(activity)
            stack.add(activity)
        }
    }

    /**
     * 获取当前Activity
     */
    fun getTopActivity(): Activity {
        synchronized(ActivityStackManager::class.java) {
            return stack.last
        }
    }

    /**
     * 获取Activity栈
     */
    fun getActivityStack(): Array<Activity> {
        return stack.toArray(arrayOfNulls(stack.size))
    }

    /**
     * 取消注册Activity
     */
    fun unregisterActivity(activity: Activity) {
        synchronized(ActivityStackManager::class.java) {
            stack.remove(activity)
        }
    }

    /**
     * 监听app前后台状态
     */
    interface OnAppBackgroundListener {
        fun onAppBackground()

        fun onAppForeground()
    }

    /**
     * 注册app监听
     */
    fun registerAppBackgroundListener(listener: OnAppBackgroundListener) {
        appBackgroundListeners.add(listener)
    }

    /**
     * 取消app监听
     */
    fun unregisterAppBackgroundListener(listener: OnAppBackgroundListener) {
        appBackgroundListeners.remove(listener)
    }
}