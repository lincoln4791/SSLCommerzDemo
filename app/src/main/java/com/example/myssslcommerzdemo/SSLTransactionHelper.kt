package com.example.myssslcommerzdemo

import android.app.Activity
import android.content.Context
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener

object SSLTransactionHelper {
    lateinit var successResponse :String
    lateinit var failedResponse :String
    lateinit var marchantErrorResponse :String

/*    override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {
        successResponse = "Success"
        val mainActivity = MainActivity()
        mainActivity.tvResponse.text = successResponse
    }

    override fun transactionFail(p0: String?) {
        failedResponse = "Failed"
    }

    override fun merchantValidationError(p0: String?) {
        marchantErrorResponse = "MarchantError"
    }*/


    fun getSSLCommerzInitializer(amount : Double,transactionId : String) : SSLCommerzInitialization {
        val sslCommerzInitialization = SSLCommerzInitialization(
            "linco615047b39a651",
            "linco615047b39a651@ssl",
            amount,
            SSLCCurrencyType.BDT,
            transactionId,
            "MyProductTypeDemo",
            SSLCSdkType.TESTBOX
        )
        return sslCommerzInitialization
    }
}