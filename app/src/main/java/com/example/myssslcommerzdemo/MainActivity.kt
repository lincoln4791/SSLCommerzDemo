package com.example.myssslcommerzdemo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager : ViewPager
    private lateinit var list : MutableList<ModelClass>
    private lateinit var mAdapter : SubscriptionAdapter

    lateinit var dialog :Dialog
    lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewPager)
        list = mutableListOf()

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


    }

     fun startTransaction( amount : String,durationType : String) {
        IntegrateSSLCommerz
            .getInstance(this@MainActivity)
            .addSSLCommerzInitialization(SSLTransactionHelper.getSSLCommerzInitializer(amount.toDouble(),"123456789098765"))
            .buildApiCall(object : SSLCTransactionResponseListener{
                override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {

                    val sharedPref = this@MainActivity.getPreferences(Context.MODE_PRIVATE) ?: return
                    with (sharedPref.edit()) {
                        putString(Constants.DURATION_TYPE,durationType)
                        putString(Constants.AMOUNT,amount)
                        putBoolean(Constants.IS_LOGGED_IN,true)
                        commit()
                    }

                    view.findViewById<TextView>(R.id.tv_transactionStatus_dialogue).text = "Success"

                    dialog.show()

                    view.findViewById<Button>(R.id.btn_ok_dialogue).setOnClickListener {
                        dialog.dismiss()
                        startActivity(Intent(this@MainActivity,PaidUserActivity::class.java))
                    }


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
            });
    }


}