package com.example.myssslcommerzdemo.googlePayDemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.billingclient.api.*
import com.example.myssslcommerzdemo.MainActivity
import com.example.myssslcommerzdemo.R
import com.example.myssslcommerzdemo.databinding.ActivityMyGooglePayDemoBinding
import com.example.myssslcommerzdemo.gPay2.GPay2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyGooglePayDemo : AppCompatActivity() {

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.

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

    val skuList : ArrayList<String> = arrayListOf<String>()


    private lateinit var binding : ActivityMyGooglePayDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyGooglePayDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        billingClient = BillingClient.newBuilder(applicationContext)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        skuList.add("test")



        binding.btnBuy.setOnClickListener {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
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
}