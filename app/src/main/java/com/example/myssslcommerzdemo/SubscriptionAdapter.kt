package com.example.myssslcommerzdemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter

class SubscriptionAdapter(var ctx: Context, var list: MutableList<ModelClass>) : PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //return super.instantiateItem(container, position)
        val mView = LayoutInflater.from(ctx).inflate(R.layout.sample_cardview,container,false)
        val mainLayout = mView.findViewById<CardView>(R.id.mainLayout_sample)
        val desc = mView.findViewById<TextView>(R.id.description_sample)
        val amount = mView.findViewById<TextView>(R.id.amount_sample)
        val title = mView.findViewById<TextView>(R.id.title_sample)
        val duration = mView.findViewById<TextView>(R.id.durationType_sample)
        val sType = mView.findViewById<TextView>(R.id.subscriptionType_sample)
        val btnSubscribe = mView.findViewById<Button>(R.id.btnSubscribe_sample)
        desc.text = list[position].description
        amount.text = list[position].amount+" BDT"
        title.text = list[position].title
        duration.text = list[position].durationType
        sType.text = list[position].subscriptionType
        container.addView(mView, 0)

        btnSubscribe.setOnClickListener {
            val context = ctx as MainActivity
            context.startTransaction(list[position].amount,list[position].durationType)
        }

        mainLayout.setOnClickListener {
            val context = ctx as MainActivity
            context.startTransaction(list[position].amount,list[position].durationType)
        }

        return mView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
        container.removeView(`object` as View)

    }

}