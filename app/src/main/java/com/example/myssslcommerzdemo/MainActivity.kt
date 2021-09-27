package com.example.myssslcommerzdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType

import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType

import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener


class MainActivity : AppCompatActivity(), SSLCTransactionResponseListener {
    private lateinit var btnStart : Button
    private lateinit var tvResponse : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btn_start)
        tvResponse = findViewById(R.id.tv_response)

        btnStart.setOnClickListener {
            startTransaction()
        }


    }

    private fun startTransaction() {

        val sslCommerzInitialization = SSLCommerzInitialization(
            "linco615047b39a651",
            "linco615047b39a651@ssl",
            20.0,
            SSLCCurrencyType.BDT,
            "123456789098765",
            "MyProductTypeDemo",
            SSLCSdkType.TESTBOX
        )


        IntegrateSSLCommerz
            .getInstance(this@MainActivity)
            .addSSLCommerzInitialization(sslCommerzInitialization)
            .buildApiCall(this);

    }

    override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {
        tvResponse.text = "Success"
    }

    override fun transactionFail(p0: String?) {
        tvResponse.text = "Failed"
    }

    override fun merchantValidationError(p0: String?) {
        tvResponse.text = "MarchantError"
    }
}