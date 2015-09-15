package com.niuan.common.ezyer.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.base.EzyerViewHolder;
import com.niuan.common.ezyer.base.annotation.EzyerView;
import com.niuan.common.ezyer.base.view.adapter.EzyerListHolderAdapter;
import com.niuan.common.ezyer.test.pojo.Item;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class MainListAdapter extends EzyerListHolderAdapter<EzyerViewHolder, Item> {

    @EzyerView(resourceId = R.layout.list_item1)
    public static class ListHolder1 extends EzyerViewHolder {
        public ListHolder1(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
            super(inflater, parent, attachToParent);
        }

        @Override
        protected void onInit() {
            pair(Item.ID_ITEM_ID, R.id.text1);
            pair(Item.ID_ITEM_NAME, R.id.text2);
            pair(Item.ID_ITEM_IMG1, R.id.img1);
            pair(Item.ID_ITEM_IMG2, R.id.img2);
            pair(Item.ID_ITEM_IMG3, R.id.img3);
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
        if (position % 3 == 0) {
            return ListHolder1.class;
        }
        return ListHolder2.class;
    }

    @Override
    public Class<EzyerViewHolder>[] initSupportHolderTypes() {
        return new Class[]{ListHolder1.class, ListHolder2.class};
    }
}
