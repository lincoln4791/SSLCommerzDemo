package com.example.myssslcommerzdemo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.myssslcommerzdemo.databinding.ActivitySecondDesignBinding
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener

class SecondDesign : AppCompatActivity() {
    lateinit var dialog : Dialog
    lateinit var view : View
    lateinit var amount: String
    lateinit var plan: String

    private lateinit var binding : ActivitySecondDesignBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondDesignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initDialogue()
        changeStatusbarColor()

        binding.cvMonthly.setOnClickListener{
            selectMonthlyPlan()
        }

        binding.cvYearly.setOnClickListener {
            selectYearlyPlan()
        }

        binding.btnSubscribe.setOnClickListener {
            startTransaction(amount,plan)
        }



    }

    private fun init(){
        binding.tvYearlyPlanAmountPrevious.paintFlags =
            binding.tvYearlyPlanAmountPrevious.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        amount = Constants.AMOUNT_MONTHLY
        plan = Constants.DURATION_TYPE_MONTHLY

    }

    private fun selectYearlyPlan() {
        binding.cvMonthly.setCardBackgroundColor(resources.getColor(R.color.white))
        binding.cvYearly.setCardBackgroundColor(resources.getColor(R.color.primary))
        binding.tvMonthlyPlan.setTextColor(resources.getColor(R.color.black_off))
        binding.tvMonthlyPlanAmount.setTextColor(resources.getColor(R.color.black))
        binding.tvYearlyPlan.setTextColor(resources.getColor(R.color.white_o))
        binding.tvYearlyPlanAmount.setTextColor(resources.getColor(R.color.white))
        binding.ivCheckYearly.visibility = View.VISIBLE
        binding.ivCheckMonthly.visibility = View.INVISIBLE
        binding.tvYearlyPlanAmountPrevious.setTextColor(resources.getColor(R.color.white_o))

        amount = Constants.AMOUNT_YEARLY
        plan = Constants.DURATION_TYPE_YEARLY


    }

    private fun selectMonthlyPlan() {
        binding.cvMonthly.setCardBackgroundColor(resources.getColor(R.color.primary))
        binding.cvYearly.setCardBackgroundColor(resources.getColor(R.color.white))
        binding.tvMonthlyPlan.setTextColor(resources.getColor(R.color.white))
        binding.tvMonthlyPlanAmount.setTextColor(resources.getColor(R.color.white))
        binding.tvYearlyPlan.setTextColor(resources.getColor(R.color.black_off))
        binding.tvYearlyPlanAmount.setTextColor(resources.getColor(R.color.black))
        binding.ivCheckYearly.visibility = View.INVISIBLE
        binding.ivCheckMonthly.visibility = View.VISIBLE
        binding.tvYearlyPlanAmountPrevious.setTextColor(resources.getColor(R.color.black_off))

        amount = Constants.AMOUNT_MONTHLY
        plan = Constants.DURATION_TYPE_YEARLY
    }

    private fun initDialogue() {
        dialog =Dialog(this@SecondDesign)
        view = LayoutInflater.from(this@SecondDesign).inflate(R.layout.dialog_transaction,null,false)
        dialog.setContentView(view)
    }


    private fun changeStatusbarColor() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.primary)
    }


    fun startTransaction( amount : String,durationType : String) {
        IntegrateSSLCommerz
            .getInstance(this@SecondDesign)
            .addSSLCommerzInitialization(SSLTransactionHelper.getSSLCommerzInitializer(amount.toDouble(),"123456789098765"))
            .buildApiCall(object : SSLCTransactionResponseListener {
                override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {

                    val sharedPref = this@SecondDesign.getPreferences(Context.MODE_PRIVATE) ?: return
                    with (sharedPref.edit()) {
                        putString(com.example.myssslcommerzdemo.Constants.DURATION_TYPE,durationType)
                        putString(com.example.myssslcommerzdemo.Constants.AMOUNT,amount)
                        putBoolean(com.example.myssslcommerzdemo.Constants.IS_LOGGED_IN,true)
                        commit()
                    }

                    view.findViewById<TextView>(R.id.tv_transactionStatus_dialogue).text = "Success"

                    dialog.show()

                    view.findViewById<Button>(R.id.btn_ok_dialogue).setOnClickListener {
                        dialog.dismiss()
                        startActivity(Intent(this@SecondDesign,PaidUserActivity::class.java))
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