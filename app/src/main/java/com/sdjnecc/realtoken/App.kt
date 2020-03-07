package com.sdjnecc.realtoken

import android.app.Application
import com.zhpan.idea.RealToken

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RealToken.init(this)
    }
}