package com.example.myssslcommerzdemo.IABV4_Subscription

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.example.myssslcommerzdemo.PrefManager
import com.example.myssslcommerzdemo.R
import com.example.myssslcommerzdemo.databinding.ActivityIabV4Binding
import com.example.myssslcommerzdemo.model.SubscriptionInfoFromGoogle
import com.google.gson.Gson


class IAB_V4 : AppCompatActivity() {

    private lateinit var billingClient : BillingClient
    private lateinit var prefManager : PrefManager

    private lateinit var binding : ActivityIabV4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        prefManager = PrefManager(this)
        super.onCreate(savedInstanceState)
        binding = ActivityIabV4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener { billingResult, list ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && list != null) {
                    for (purchase in list) {
                        verifySubPurchase(purchase)
                    }
                }
            }.build()

        //start the connection after initializing the billing client

        //start the connection after initializing the billing client
        establishConnection()

        binding.btnMonthly.setOnClickListener {
            initPurchase("test_subscription2")
        }

        binding.btn3month.setOnClickListener {
            initPurchase("test_subscription3")
        }

    }

    fun establishConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    checkSubscription()
                    showProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                establishConnection()
            }
        })
    }


    fun showProducts() {
        binding.btn3month.visibility = View.VISIBLE
        binding.btnMonthly.visibility = View.VISIBLE
    }

    private fun initPurchase(productID : String) {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(productID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

        billingClient.querySkuDetailsAsync(params.build()
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                // Process the result.
                for (skuDetails in skuDetailsList) {
                    if (skuDetails.sku == productID) {
                        //Now update the UI
                        launchPurchaseFlow(skuDetails)
                    }
                }
            }
        }
    }


    fun launchPurchaseFlow(skuDetails: SkuDetails?) {
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails!!)
            .build()
        billingClient.launchBillingFlow(this@IAB_V4, billingFlowParams)
    }


    fun verifySubPurchase(purchases: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchases.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(acknowledgePurchaseParams
        ) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                //Toast.makeText(SubscriptionActivity.this, "Item Consumed", Toast.LENGTH_SHORT).show();
                // Handle the success of the consume operation.
                //user prefs to set premium
                Toast.makeText(this@IAB_V4, "You are a premium user now", Toast.LENGTH_SHORT)
                    .show()
                //updateUser();

                //Setting premium to 1
                // 1 - premium
                //0 - no premium
                prefManager.isPremium = true
            }
        }
        Log.d("tag", "Purchase Token: " + purchases.purchaseToken)
        Log.d("tag", "Purchase Time: " + purchases.purchaseTime)
        Log.d("tag", "Purchase OrderID: " + purchases.orderId)
    }


    override fun onResume() {
        super.onResume()
        billingClient.queryPurchasesAsync(
            BillingClient.SkuType.SUBS
        ) { billingResult, list ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in list) {
                    if (purchase.purchaseState === Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        verifySubPurchase(purchase)
                    }
                }
            }
        }
    }


    fun checkSubscription() {
        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS) { billingResult: BillingResult, purchases: MutableList<Purchase> ->
            Log.d("Billing",
                "Billing Result -> ${billingResult.responseCode} ::: Purchased Product Length- -> ${purchases.size}")
            binding.tv.text = "Currenty active Subscription -> ${purchases.size} "
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                Log.d("Billing", "Purchased Product Length -> ${purchases.size}")
                for (purchase in purchases) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        val g = Gson()
                        val purchaseInfo = g.fromJson(purchase.originalJson,
                            SubscriptionInfoFromGoogle::class.java)
                        Log.d("Billing",
                            "Purchased Products are -> ${purchase.purchaseToken}::: product id -> ${purchaseInfo.productId}")
                    }
                }
            }
        }

    }

}