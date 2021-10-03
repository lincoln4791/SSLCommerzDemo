package com.example.myssslcommerzdemo.retrorfit

import com.example.myssslcommerzdemo.RefundModel
import com.example.myssslcommerzdemo.TransactionQueryResponseByTrxID
import com.example.myssslcommerzdemo.TransactionQueryResponseModel
import com.example.myssslcommerzdemo.response_status.IPN_VALIDATIOR
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface MyApi {

    @GET("validator/api/validationserverAPI.php?")
    fun getIPNData(@Query("val_id" ) val_id : String,
                   @Query("store_id" ) store_id : String,
                   @Query("store_passwd" ) store_passwd : String): Call<IPN_VALIDATIOR>?




    @GET("validator/api/merchantTransIDvalidationAPI.php")
    fun getRefund(@Query("bank_tran_id" ) bank_tran_id : String,
                   @Query("fund_amount" ) fund_amount : String,
                   @Query("refund_remarks" ) refund_remarks : String): Call<RefundModel>?


    @GET("validator/api/merchantTransIDvalidationAPI.php?")
    fun getTransactionQuery(@Query("sessionkey" ) sessionkey : String,
                            @Query("store_id" ) store_id : String,
                            @Query("store_passwd" ) store_passwd : String): Call<TransactionQueryResponseModel>?


    @GET("validator/api/merchantTransIDvalidationAPI.php?")
    fun getTransactionQuerybyTrxID(@Query("tran_id" ) tran_id : String,
                            @Query("store_id" ) store_id : String,
                            @Query("store_passwd" ) store_passwd : String): Call<TransactionQueryResponseByTrxID>?


}