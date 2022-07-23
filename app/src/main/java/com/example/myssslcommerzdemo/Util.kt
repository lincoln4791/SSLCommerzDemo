package com.example.myssslcommerzdemo

import java.math.BigInteger
import java.security.MessageDigest

class Util {

    companion object{
        fun getMD5FromString(input:String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }

}