package com.example.myssslcommerzdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myssslcommerzdemo.R
import com.example.myssslcommerzdemo.googlePayDemo.MyGooglePayDemo
import com.example.myssslcommerzdemo.model.SubscriptionModelClass

class Adapter_IAB_Packages(val context : Context,val data: SubscriptionModelClass) : RecyclerView.Adapter<Adapter_IAB_Packages.MyViewHolder>() {

    val ctx = context as MyGooglePayDemo

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.sample_subscription_plan,null,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName.text = data.subscriptions[position].product_name
        holder.tvPrice.text = data.subscriptions[position].price

        holder.cvSubscribe.setOnClickListener {
            ctx.startSubscriptionProcess(data.subscriptions[position].product_id)
        }

    }

    override fun getItemCount(): Int {
       return data.subscriptions.size
    }

    class MyViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val tvPrice : TextView = view.findViewById(R.id.tv_price)
        val tvName : TextView = view.findViewById(R.id.tv_name)
        val cvSubscribe : CardView = view.findViewById(R.id.cv_subscribe)
    }
}