package com.example.myssslcommerzdemo

data class RefundModel(
    val APIConnect: String,
    val bank_tran_id: String,
    val errorReason: String,
    val refund_ref_id: String,
    val status: String,
    val trans_id: String
)