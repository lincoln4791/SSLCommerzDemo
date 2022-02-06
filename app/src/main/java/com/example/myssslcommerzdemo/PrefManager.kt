package com.example.myssslcommerzdemo

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context : Context) {

    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // shared pref mode
    private var PRIVATE_MODE = 0

    init {
        pref = context.getSharedPreferences("SSL", PRIVATE_MODE)
        editor = pref.edit()
    }

    var isPremium:Boolean
        get() = pref.getBoolean("isPremium",false)
        set(value) {editor.putBoolean("isPremium",value).commit()}

}