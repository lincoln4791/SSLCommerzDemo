package com.example.myssslcommerzdemo

data class TransactionQueryResponseByTrxID(
    val APIConnect: String,
    val element: List<Element>,
    val no_of_trans_found: Int
)