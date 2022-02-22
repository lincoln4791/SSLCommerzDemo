package com.example.myssslcommerzdemo.googlePayDemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.example.myssslcommerzdemo.MainActivity
import com.example.myssslcommerzdemo.R
import com.example.myssslcommerzdemo.databinding.ActivityMyGooglePayDemoBinding
import com.example.myssslcommerzdemo.gPay2.GPay2
import com.example.myssslcommerzdemo.model.SubscriptionModelClass
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyGooglePayDemo : AppCompatActivity() {

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            Toast.makeText(this@MyGooglePayDemo,"Check Subscription Callback",Toast.LENGTH_SHORT).show()

            if(billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases!=null ){
                for(purchase in purchases){
                    if(purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged){
                        Toast.makeText(this@MyGooglePayDemo,"Purchase Successful",Toast.LENGTH_SHORT).show()
                    }

                }
            }
            else{
                Toast.makeText(this@MyGooglePayDemo,"Something Went Wrong In Purchase",Toast.LENGTH_SHORT).show()
            }

        }

    private lateinit var billingClient : BillingClient
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private var subscriptionData: SubscriptionModelClass? = null
    private var isAlreadySubscribed = false

    private lateinit var binding : ActivityMyGooglePayDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyGooglePayDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        billingClient = BillingClient.newBuilder(applicationContext)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

       // initRemoteConfig()

        //skuList.add("test")

       /* skuList.add("test_subscription5")
        skuList.add("test_subscription4")*/



        binding.btnBuy.setOnClickListener {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        val skuList : ArrayList<String> = arrayListOf<String>()
                        skuList.add("test")
                        val params = SkuDetailsParams.newBuilder()
                        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

                        // leverage querySkuDetails Kotlin extension function
                       billingClient.querySkuDetailsAsync(params.build()){
                               billingResult, mutableList->

                           for(skuDetails in mutableList!!){
                               val flowParams = BillingFlowParams.newBuilder()
                                   .setSkuDetails(skuDetails)
                                   .build()
                               val responseCode = billingClient.launchBillingFlow(this@MyGooglePayDemo, flowParams).responseCode
                           }

                       }

                    }
                }
                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }


        binding.btnGoToSubscription.setOnClickListener {
            startActivity(Intent(this@MyGooglePayDemo,GPay2::class.java))
        }

        binding.btnSubscribe.setOnClickListener {

        }

    }


    suspend fun querySkuDetails() {
        val skuList = ArrayList<String>()
        skuList.add("premium_upgrade")
        skuList.add("gas")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)

        // leverage querySkuDetails Kotlin extension function
        val skuDetailsResult = withContext(Dispatchers.IO) {
            billingClient.querySkuDetails(params.build())
        }

        // Process the result.
    }


    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        CoroutineScope(Dispatchers.IO).launch {
            checkSubscriptionStatus()
        }
        super.onResume()
    }


    private suspend fun checkSubscriptionStatus(){
        /*Handler(Looper.getMainLooper()).post {
            Toast.makeText(this@MyGooglePayDemo, "Check Subscription Called", Toast.LENGTH_SHORT)
                .show()
        }*/

        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS){ billingResult: BillingResult, mutableList: MutableList<Purchase> ->

        }
    }

    fun startSubscriptionProcess(productID :String){
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    val skuList : ArrayList<String> = arrayListOf<String>()
                    skuList.add(productID)
                    // The BillingClient is ready. You can query purchases here.
                    val params = SkuDetailsParams.newBuilder()
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

                    // leverage querySkuDetails Kotlin extension function
                    billingClient.querySkuDetailsAsync(params.build()){
                            billingResult, mutableList->

                        for(skuDetails in mutableList!!){
                            val flowParams = BillingFlowParams.newBuilder()
                                .setSkuDetails(skuDetails)
                                .build()
                            val responseCode = billingClient.launchBillingFlow(this@MyGooglePayDemo, flowParams).responseCode
                        }

                    }

                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }



/*    private fun initRemoteConfig() {
        Log.d("tag","Remote Config Inited")
        firebaseRemoteConfig = Firebase.remoteConfig
        val configSettings  = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        //firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_cofig_defaults)

        //val cacheExpiration: Long = 3600 // 1 hour in seconds.

        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("Remote", "Config params updated: $updated")

                    //subscriptionData = firebaseRemoteConfig.getString("iab_subscriptions")
                    //Log.d("Remote","Data fetched -> ${firebaseRemoteConfig.getString("iab_subscriptions")} ")

                    *//*   Toast.makeText(this, "Fetch and activate succeeded",
                           Toast.LENGTH_SHORT).show()*//*
                } else {
                    Log.d("Remote", "fetch Failed")
                    *//*  Toast.makeText(this, "Fetch failed",
                          Toast.LENGTH_SHORT).show()*//*
                }
                //displayWelcomeMessage()
            }


    }*/


}