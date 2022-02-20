package com.example.myssslcommerzdemo.model

data class SubscriptionInfoFromGoogle(
    val acknowledged: Boolean,
    val autoRenewing: Boolean,
    val orderId: String,
    val packageName: String,
    val productId: String,
    val purchaseState: Int,
    val purchaseTime: Long,
    val purchaseToken: String,
    val quantity: Int
)