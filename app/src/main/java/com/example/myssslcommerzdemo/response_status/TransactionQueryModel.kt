package com.example.myssslcommerzdemo.response_status

import java.io.Serializable

class TransactionQueryModel :Serializable {
     var sessionkey:String? = null
     var tran_id : String? = null
     var store_id: String? = null
     var store_passwd : String? = null

    constructor(sessionkey: String, store_id : String, store_passwd : String){
        this.sessionkey = sessionkey
        this.store_id = store_id
        this.store_passwd = store_passwd
    }


}