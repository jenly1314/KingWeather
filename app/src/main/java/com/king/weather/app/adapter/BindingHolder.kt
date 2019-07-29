package com.king.weather.app.adapter

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.king.base.adapter.holder.ViewHolder

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class BindingHolder<VDB : ViewDataBinding>(convertView: View) : ViewHolder(convertView) {

    var mBinding: VDB? = null

    init {
        mBinding = DataBindingUtil.bind(convertView)
    }


}