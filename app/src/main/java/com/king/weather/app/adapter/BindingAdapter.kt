package com.king.weather.app.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.king.base.adapter.BaseRecyclerAdapter
import com.king.weather.BR

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
open class BindingAdapter<T, VDB : ViewDataBinding>(context: Context, layoutId: Int) :
    BaseRecyclerAdapter<T, BindingHolder<VDB>>(context, layoutId) {


    override fun bindViewDatas(holder: BindingHolder<VDB>?, item: T, position: Int) {
        holder?.mBinding?.let {
            it.setVariable(BR.data, item)
            it.executePendingBindings()
        }
    }

    fun getItem(position: Int): T{
        return listData[position]
    }

    fun refreshData(list: MutableList<T>?) {
        list?.run {
            listData = list
        } ?: run {
            listData.clear()
        }

        notifyDataSetChanged()
    }

}