package com.example.myssslcommerzdemo.GPayConversionSDK

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myssslcommerzdemo.databinding.ActivityGpayConversionSdkBinding
import com.qonversion.android.sdk.*
import com.qonversion.android.sdk.dto.QPermission
import com.qonversion.android.sdk.dto.offerings.QOfferings
import com.qonversion.android.sdk.dto.products.QProduct
import com.qonversion.android.sdk.dto.products.QProductRenewState

class GPayConversionSDK : AppCompatActivity() {

    private lateinit var binding : ActivityGpayConversionSdkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpayConversionSdkBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.getListOfAvailableProducts.setOnClickListener {
            getListOfAvailableProducts()
        }

        binding.getOfferingById.setOnClickListener {
            getOfferingById()
        }

        binding.displayProducts.setOnClickListener {
            displayingProducts()
        }

        binding.restorePreviousPurchase.setOnClickListener {
            restorePreviousPurchase()
        }


        checkUserSubscriptionStatus()


    }

    private fun displayingProducts(){
        Qonversion.offerings(object: QonversionOfferingsCallback {
            override fun onSuccess(offerings: QOfferings) {
                val mainOffering = offerings.main
                if (mainOffering != null && mainOffering.products.isNotEmpty()) {
                    // Display products for sale
                    Toast.makeText(this@GPayConversionSDK,"Products for sell",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@GPayConversionSDK,"Null or empty , Products for sell",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onError(error: QonversionError) {
                // handle error here
                Toast.makeText(this@GPayConversionSDK,"Error, Products for sell -> ${error.additionalMessage}",Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getOfferingById(){

        Qonversion.offerings(object: QonversionOfferingsCallback {
            override fun onSuccess(offerings: QOfferings) {
                val offering = offerings.offeringForID("test_subscription2")
                if (offering != null) {
                    // offering is available
                    Toast.makeText(this@GPayConversionSDK,"get offering by id",Toast.LENGTH_SHORT).show()

                    for(product in offering.products){

                    }

                    offering.products

                }
                else{
                    Toast.makeText(this@GPayConversionSDK,"Null or empty,get offering by id ",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onError(error: QonversionError) {
                Toast.makeText(this@GPayConversionSDK,"error, get offering by id -> ${error.additionalMessage}",Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun getListOfAvailableProducts(){

        Qonversion.products(callback = object: QonversionProductsCallback {
            override fun onSuccess(products: Map<String, QProduct>) {
                // handle available products here
                Toast.makeText(this@GPayConversionSDK,"get List of available products",Toast.LENGTH_SHORT).show()
                val prod =products["test_subscription2"]
                if(prod != null){
                    makePurchase(prod)
                }
                else{
                    Toast.makeText(this@GPayConversionSDK,"null product",Toast.LENGTH_SHORT).show()
                }


            }

            override fun onError(error: QonversionError) {
                // handle error here
                Toast.makeText(this@GPayConversionSDK,"error,get List of available products-> ${error.additionalMessage}",Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun makePurchase(product : QProduct){
        Qonversion.purchase(this, product, callback = object: QonversionPermissionsCallback {
            override fun onSuccess(permissions: Map<String, QPermission>) {
                val premiumPermission = permissions["premium"]
                if (premiumPermission != null && premiumPermission.isActive()) {
                    // handle active permission here
                }
            }

            override fun onError(error: QonversionError) {
                // handle error here
            }
        })
    }



    private fun checkUserSubscriptionStatus(){
        Qonversion.checkPermissions(object: QonversionPermissionsCallback {
            override fun onSuccess(permissions: Map<String, QPermission>) {
                val premiumPermission = permissions["Premium"]
                if (premiumPermission != null && premiumPermission.isActive()) {
                    // handle active permission here

                    // also you can check renew state if needed
                    // for example to check if user has canceled subscription and offer him a discount
                    when (premiumPermission.renewState) {
                        QProductRenewState.NonRenewable -> Toast.makeText(this@GPayConversionSDK,"NonRenewable",Toast.LENGTH_SHORT).show()
                        QProductRenewState.WillRenew -> Toast.makeText(this@GPayConversionSDK,"WillRenew",Toast.LENGTH_SHORT).show()
                            // WillRenew is the state of an auto-renewable subscription
                            // NonRenewable is the state of consumable/non-consumable IAPs that could unlock lifetime access
                            QProductRenewState.BillingIssue -> Toast.makeText(this@GPayConversionSDK,"BillingIssue",Toast.LENGTH_SHORT).show()
                        // Prompt the user to update the payment method.
                        QProductRenewState.Canceled -> Toast.makeText(this@GPayConversionSDK,"Canceled",Toast.LENGTH_SHORT).show()
                        // The user has turned off auto-renewal for the subscription, but the subscription has not expired yet.
                        // Prompt the user to resubscribe with a special offer.
                    }
                }
                else{
                    Toast.makeText(this@GPayConversionSDK,"permission null",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(error: QonversionError) {
                // handle error here
                Toast.makeText(this@GPayConversionSDK,"error, Permission -> ${error.additionalMessage}",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun restorePreviousPurchase(){

        Qonversion.restore(object : QonversionPermissionsCallback {
            override fun onSuccess(permissions: Map<String, QPermission>) {
                val premiumPermission = permissions["Premium"]
                if (premiumPermission != null && premiumPermission.isActive()) {
                    // handle active permission here

                    Toast.makeText(this@GPayConversionSDK,"Restore Previous Purchase",Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(this@GPayConversionSDK,"null , Restore Previous Purchase",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(error: QonversionError) {
                // handle error here
                Toast.makeText(this@GPayConversionSDK,"Error Previous Purchase",Toast.LENGTH_SHORT).show()
            }
        })

    }

}