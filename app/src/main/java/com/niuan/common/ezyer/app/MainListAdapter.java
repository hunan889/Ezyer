package com.niuan.common.ezyer.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.app.pojo.Dish;
import com.niuan.common.ezyer.app.pojo.NetStruct;
import com.niuan.common.ezyer.ui.annotation.EzyerView;
import com.niuan.common.ezyer.ui.view.adapter.EzyerListHolderAdapter;
import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;

/**
 * Created by Carlos Liu on 2015/8/15.
 */
public class MainListAdapter extends EzyerListHolderAdapter<EzyerViewHolder, NetStruct.Data> {

    @EzyerView(resourceId = R.layout.list_item1)
    public static class ListHolder1 extends EzyerViewHolder {
        public ListHolder1(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
            super(inflater, parent, attachToParent);
            pair(Dish.ID_AUTHOR, R.id.dish_author);
            pair(Dish.ID_IMG, R.id.dish_img);
            pair(Dish.ID_METHOD, R.id.dish_method);
            pair(Dish.ID_NAME, R.id.dish_name);
            pair(Dish.ID_TIME, R.id.dish_time);
        }
    }

//    @EzyerView(resourceId = R.layout.list_item2)
//    public static class ListHolder2 extends EzyerViewHolder {
//        public ListHolder2(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
//            super(inflater, parent, attachToParent);
//        }
//
//        @Override
//        protected void onInit() {
//            pair(Item.ID_ITEM_ID, R.id.text1);
//            pair(Item.ID_ITEM_NAME, R.id.text2);
//        }
//    }

    public MainListAdapter(Context context) {
        super(context);
    }

    @Override
    public Class<? extends EzyerViewHolder> getHolderType(int position) {
        return ListHolder1.class;
    }

    @Override
    public Class<EzyerViewHolder>[] initSupportHolderTypes() {
        return new Class[]{ListHolder1.class};
    }
}
