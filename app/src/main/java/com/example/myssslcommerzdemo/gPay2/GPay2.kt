package com.example.myssslcommerzdemo.gPay2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.example.myssslcommerzdemo.R
import com.example.myssslcommerzdemo.databinding.ActivityGpay2Binding
import com.example.myssslcommerzdemo.googlePayDemo.MyGooglePayDemo

class GPay2 : AppCompatActivity(),BillingProcessor.IBillingHandler {

    private lateinit var billingProcessor : BillingProcessor
    private var purchaseInfo : PurchaseInfo? = null

    private lateinit var binding : ActivityGpay2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpay2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        billingProcessor = BillingProcessor(this,"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwhWfKCEJY5UC8E5TXgB4h/AMjvRDMaKTP8BKvbgHhT1XCJ2+cYY+vnYNwZnk3KAJLJwMgEVTj1yu4kFxqgiD5r4Gg0Q6rpECW8GSg8cMWd6nKngXzz/D4FjRTwMxzWxMEGLS0iw/JRBSg+8fjWCH7qdx9SKcpDUFF90zoASyWzSTWLND2UlKO/UHmRgBLlA58gISFubpIyod45uCO2htvqkjn6anoywh8Few66Hp3pBNYYUDkEy0shhcsZg4mswc7S7CSU30tmJpfS7AsS7rPPWI2JQxAurKn4iOjf1fAtv5bfRGUqQSPvjmjpOhHvEb10/kf5v3kPVkrhwjCaH9twIDAQAB",this)
        billingProcessor.initialize()

        binding.btnSubscribe.setOnClickListener {
            billingProcessor.subscribe(this,"test_subscription5")
        }

        binding.backToOneTimePurchase.setOnClickListener {
            startActivity(Intent(this@GPay2,MyGooglePayDemo::class.java))
        }



    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {

        Toast.makeText(this@GPay2,"Purchased",Toast.LENGTH_SHORT).show()
    }

    override fun onPurchaseHistoryRestored() {

    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {

    }

    override fun onBillingInitialized() {


        billingProcessor.loadOwnedPurchasesFromGoogleAsync(object : BillingProcessor.IPurchasesResponseListener{
            override fun onPurchasesSuccess() {
                //Toast.makeText(this@GPay2,"Purchase Success",Toast.LENGTH_SHORT).show()
            }

            override fun onPurchasesError() {
                //Toast.makeText(this@GPay2,"Purchase Failed",Toast.LENGTH_SHORT).show()
            }

        })

        purchaseInfo = billingProcessor.getSubscriptionPurchaseInfo("test_subscription5")

        if(purchaseInfo!=null){
            if(purchaseInfo!!.purchaseData.autoRenewing){
              binding.tv.text ="token -> ${purchaseInfo!!.purchaseData.purchaseToken} \npurchase date -> ${purchaseInfo!!.purchaseData.purchaseTime} " +
                      "\n order id: ${purchaseInfo!!.purchaseData.orderId} \npurchase state name ${purchaseInfo!!.purchaseData.purchaseState.name}"
                Toast.makeText(this@GPay2,"Already Subscribed",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this@GPay2,"Not Subscribed",Toast.LENGTH_SHORT).show()
            }
            binding.tv.text = purchaseInfo!!.responseData
        }
        else{
            Toast.makeText(this@GPay2,"Expired",Toast.LENGTH_SHORT).show()
        }


    }

    override fun onDestroy() {
        if(billingProcessor!=null){
            billingProcessor.release()

        }
        super.onDestroy()
    }
}