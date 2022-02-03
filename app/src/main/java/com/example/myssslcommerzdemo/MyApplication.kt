package com.example.myssslcommerzdemo

import android.app.Application
import com.qonversion.android.sdk.Qonversion

class MyApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            Qonversion.launch(this, "AQeHY8I_d4OGV1Y6Qnoza4x2KeBxxwE1", false)
        }

}