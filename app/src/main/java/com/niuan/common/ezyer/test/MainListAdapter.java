package com.niuan.common.ezyer.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;
import com.niuan.common.ezyer.ui.annotation.EzyerView;
import com.niuan.common.ezyer.ui.view.adapter.EzyerListHolderAdapter;
import com.niuan.common.ezyer.test.pojo.Item;
import com.niuan.common.ezyer.test.pojo.NetStruct;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class MainListAdapter extends EzyerListHolderAdapter<EzyerViewHolder, NetStruct.Data> {

    @EzyerView(resourceId = R.layout.list_item1)
    public static class ListHolder1 extends EzyerViewHolder {
        public ListHolder1(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
            super(inflater, parent, attachToParent);
        }

        @Override
        protected void onInit() {
            pair(NetStruct.ID_LIST_PAGE_SIZE, R.id.text1);
            pair(NetStruct.ID_LIST_NAME, R.id.text2);
            pair(NetStruct.ID_LIST_ICON, R.id.img1);
        }
    }

    @EzyerView(resourceId = R.layout.list_item2)
    public static class ListHolder2 extends EzyerViewHolder {
        public ListHolder2(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
            super(inflater, parent, attachToParent);
        }

        @Override
        protected void onInit() {
            pair(Item.ID_ITEM_ID, R.id.text1);
            pair(Item.ID_ITEM_NAME, R.id.text2);
        }
    }

    public MainListAdapter(Context context) {
        super(context);
    }

    @Override
    public Class<? extends EzyerViewHolder> getHolderType(int position) {
        return ListHolder1.class;
    }

    @Override
    public Class<EzyerViewHolder>[] initSupportHolderTypes() {
        return new Class[]{ListHolder1.class, ListHolder2.class};
    }
}
