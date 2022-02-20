package com.example.myssslcommerzdemo.model

data class Subscription(
    val benifits: List<String>?,
    val billing_period: String?,
    val free_trail: String?,
    val introductory_price: String?,
    val price: String,
    val product_description: String?,
    val product_id: String,
    val product_name: String,
    val status: String?
)