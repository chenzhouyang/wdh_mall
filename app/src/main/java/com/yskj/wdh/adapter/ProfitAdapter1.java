package com.yskj.wdh.adapter;

import android.content.Context;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.ProfitBean1;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.util.StringUtils;
import com.yskj.wdh.util.ViewHolder;


/**
 * Created by wdx on 2016/11/12 0012.
 */
public class ProfitAdapter1 extends CommonAdapter<ProfitBean1.DataBean.ListBean> {
    private LoadingCaches aCache = LoadingCaches.getInstance();
    public ProfitAdapter1(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder vh, final ProfitBean1.DataBean.ListBean item) {
       vh.setText(R.id.txt_id1,item.getConsumeDate());
        vh.setText(R.id.txt_id2,item.getCount()+"");
        vh.setText(R.id.txt_id3, StringUtils.getStringtodouble(item.getPayAmount()));



    }

    public boolean containsById(long id) {
        for (int i = 0; i < getCount(); i++) {
            ProfitBean1.DataBean.ListBean mBean = new ProfitBean1.DataBean.ListBean();
            if (mBean != null && mBean.getCount() == id) {
                return true;
            }
        }
        return false;
    }


    public boolean containsById(Double count) {
        return false;
    }
}