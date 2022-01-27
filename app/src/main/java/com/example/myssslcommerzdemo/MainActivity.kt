package com.example.myssslcommerzdemo

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.android.billingclient.api.*
import com.example.myssslcommerzdemo.response_status.IPN_VALIDATIOR
import com.example.myssslcommerzdemo.retrorfit.MyApi
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCCustomerInfoInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCEMITransactionInitializer

import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCProductInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCShipmentInfoInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager
    private lateinit var list : MutableList<ModelClass>
    private lateinit var mAdapter : SubscriptionAdapter
    private lateinit var tvSubscriptionStatus : TextView
    private lateinit var btnStatus : Button



    lateinit var dialog :Dialog
    lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewPager)
        list = mutableListOf()
        tvSubscriptionStatus = findViewById(R.id.tv_subscriptionStatus)
        btnStatus = findViewById(R.id.btn_transactionStatus)

        dialog =Dialog(this@MainActivity)
        view = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_transaction,null,false)
        dialog.setContentView(view)

        //val m1 = ModelClass("Monthly","0","Trail","Limited Use","Selected Features")
        val m2 = ModelClass(Constants.DURATION_TYPE_DAILY,Constants.AMOUNT_DAILY,"Paid","Unlimited Use","All features unlocked")
        val m3 = ModelClass(Constants.DURATION_TYPE_WEEKLY,Constants.AMOUNT_WEEKLY,"Paid","Unlimited Use","All features unlocked")
        val m4 = ModelClass(Constants.DURATION_TYPE_MONTHLY,Constants.AMOUNT_MONTHLY,"Paid","Unlimited Use","All features unlocked")
        val m5 = ModelClass(Constants.DURATION_TYPE_YEARLY,Constants.AMOUNT_YEARLY,"Paid","Unlimited Use","All features unlocked")
        //list.add(m1)
        list.add(m2)
        list.add(m3)
        list.add(m4)
        list.add(m5)

        mAdapter = SubscriptionAdapter(this@MainActivity,list)
        viewPager.adapter=mAdapter






        /*btnStart.setOnClickListener {
            startTransaction()
        }*/


        btnStatus.setOnClickListener {

            //validateWithIPN()
            //getRefund()
            getTransactionQueryByTrxid()

        }



    }

    private fun getTransactionQuery() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.IPN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyApi::class.java)
        val call = service.getTransactionQuery(Constants.SESSION_KEY,Constants.STORE_ID,Constants.STORE_PASS)
        call!!.enqueue(object : Callback<TransactionQueryResponseModel>{
            override fun onResponse(call: Call<TransactionQueryResponseModel>, response: Response<TransactionQueryResponseModel>) {
                tvSubscriptionStatus.text = " ${response.body()?.status?:"null"} and ${response.body()?.bank_tran_id?:"null"}  "
            }

            override fun onFailure(call: Call<TransactionQueryResponseModel>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }




    private fun getTransactionQueryByTrxid() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.IPN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyApi::class.java)
        val call = service.getTransactionQuerybyTrxID(Constants.SESSION_KEY,Constants.STORE_ID,Constants.STORE_PASS)
        call!!.enqueue(object : Callback<TransactionQueryResponseByTrxID>{
            override fun onResponse(call: Call<TransactionQueryResponseByTrxID>, response: Response<TransactionQueryResponseByTrxID>) {
                var count = 0
                for(i in 0 until (response.body()?.element?.size!!)){
                    count++
                }

                tvSubscriptionStatus.text = " $count "
            }

            override fun onFailure(call: Call<TransactionQueryResponseByTrxID>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }



    private fun getRefund() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.IPN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyApi::class.java)
        val call = service.getRefund(Constants.BANK_TRANS_ID,Constants.TRANSECTED_AMOUNT,"I am the testt remarks")
        call!!.enqueue(object : Callback<RefundModel>{
            override fun onResponse(call: Call<RefundModel>, response: Response<RefundModel>) {
                tvSubscriptionStatus.text = "${response.body()!!.status} and ${response.body()?.errorReason?:"no eooor"} bamk trans id${response.body()!!.bank_tran_id}," +
                        " transID ${response.body()?.trans_id?:"null"} refer id ${response.body()?.refund_ref_id?:"null"} "
            }

            override fun onFailure(call: Call<RefundModel>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }




    private fun validateWithIPN() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.IPN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MyApi::class.java)
        val call = service.getIPNData(Constants.VAL_ID, Constants.STORE_ID, Constants.STORE_PASS)
        call!!.enqueue(object : Callback<IPN_VALIDATIOR>{
            override fun onResponse(
                call: Call<IPN_VALIDATIOR>,
                response: Response<IPN_VALIDATIOR>
            ) {
                tvSubscriptionStatus.text = "${response.body()!!.status} bankID ${response.body()!!.bank_tran_id}"
            }

            override fun onFailure(call: Call<IPN_VALIDATIOR>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun startTransaction( amount : String,durationType : String) {
        IntegrateSSLCommerz
            .getInstance(this@MainActivity)
            .addSSLCommerzInitialization(SSLTransactionHelper.getSSLCommerzInitializer(amount.toDouble(),"1"))
            .addCustomerInfoInitializer(getCustomerInitializer())
            //.addEMITransactionInitializer(getEMIInitializer())
            .addProductInitializer(getProductInitializer())
            //.addShipmentInfoInitializer(getShipmentInitializer())
            .buildApiCall(object : SSLCTransactionResponseListener{
                override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {

                    view.findViewById<TextView>(R.id.tv_transactionStatus_dialogue).text = "Success"

                    //dialog.show()

                    view.findViewById<Button>(R.id.btn_ok_dialogue).setOnClickListener {
                        dialog.dismiss()
                        startActivity(Intent(this@MainActivity,PaidUserActivity::class.java))
                    }

                    tvSubscriptionStatus.text = p0!!.valId
                    Constants.VAL_ID = p0.valId
                    Constants.BANK_TRANS_ID = p0.bankTranId
                    Constants.TRANSECTED_AMOUNT = p0.amount
                    Constants.SESSION_KEY = p0.sessionkey

                    Log.d("tag","Detailss ${p0} ")

                }

                override fun transactionFail(p0: String?) {
                    view.findViewById<TextView>(R.id.tv_transactionStatus_dialogue).text = "Failed"

                    dialog.show()

                    view.findViewById<Button>(R.id.btn_ok_dialogue).setOnClickListener {
                        dialog.dismiss()
                    }

                    Log.d("tag",p0!!)

                }
                override fun merchantValidationError(p0: String?) {
                }
            })
    }


    private fun getCustomerInitializer(): SSLCCustomerInfoInitializer {
        return SSLCCustomerInfoInitializer(
            "I am customerName", "customer47@mail.com",
            "i am address", "i am city", "4747", "Bangladesh", "01670878110"
        )
    }

    private fun getEMIInitializer(): SSLCEMITransactionInitializer {
        return SSLCEMITransactionInitializer(1)
    }

    private fun getProductInitializer(): SSLCProductInitializer {
        return SSLCProductInitializer(
            "Apple", "Food",
            SSLCProductInitializer.ProductProfile.General("Fruit","to eat")
        )
    }


    private fun getShipmentInitializer(): SSLCShipmentInfoInitializer {
        return SSLCShipmentInfoInitializer(
            "Courier",
            2, SSLCShipmentInfoInitializer.ShipmentDetails(
                "Iamtheshipname", "Address 1",
                "Dhaka", "1000", "BD"
            )
        )
    }





}