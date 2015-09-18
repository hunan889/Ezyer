package com.niuan.common.ezyer.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.niuan.common.ezyer.R;
import com.niuan.common.ezyer.base.EzyerEntry;
import com.niuan.common.ezyer.base.EzyerViewHolder;
import com.niuan.common.ezyer.base.annotation.EzyerView;
import com.niuan.common.ezyer.base.fragment.EzyerSimpleFragment;
import com.niuan.common.ezyer.base.view.InfiniteBannerView;
import com.niuan.common.ezyer.base.view.adapter.EzyerDataViewAdapter;
import com.niuan.common.ezyer.test.pojo.ItemList;
import com.niuan.common.ezyer.test.pojo.NetStruct;
import com.niuan.common.ezyer.test.pojo.User;
import com.niuan.common.ezyernet.EzyerParseJsonRequest;
import com.niuan.common.ezyernet.ResponseListener;

/**
 * Created by Carlos on 2015/9/14.
 */
public class MainFragment extends EzyerSimpleFragment {
    private static class EzyerViewHolder1 extends EzyerViewHolder {
        @EzyerView(resourceId = R.id.main_banner, dataId = ItemList.ID_LIST_LIST)
        InfiniteBannerView mBannerView;

        public EzyerViewHolder1(View view) {
            super(view);
        }

        @Override
        protected void onInit() {
            pair(ItemList.ID_LIST_ID, R.id.customer_item_list_id_1);
            pair(ItemList.ID_LIST_NAME, R.id.customer_item_list_name_1);
            pair(ItemList.ID_LIST_LIST, R.id.customer_products_1);
            ListView list1 = findViewById(R.id.customer_products_1);
            list1.setAdapter(new MainListAdapter(getView().getContext()));

            mBannerView.setListAdapter(new MainListAdapter(getView().getContext()));
            mBannerView.setDuration(10000);
            mBannerView.startScroll();
        }
    }

    private static class TestFragmentViewHolder extends EzyerViewHolder {

        @EzyerView(dataId = User.ID_ITEM_LIST_WRAPPER_1)
        EzyerViewHolder1 mHolder1;

        public TestFragmentViewHolder(View view) {
            super(view);
        }

        @Override
        protected void onInit() {
            pair(User.ID_USER_ID, R.id.customer_id);
            pair(User.ID_NAME, R.id.customer_name);
            pair(User.ID_ITEM_LIST_WRAPPER_2, new EzyerViewHolder(getView()) {
                @Override
                protected void onInit() {
                    pair(ItemList.ID_LIST_ID, R.id.customer_item_list_id_2);
                    pair(ItemList.ID_LIST_NAME, R.id.customer_item_list_name_2);
                    pair(ItemList.ID_LIST_LIST, R.id.customer_products_2);
                    ListView list2 = findViewById(R.id.customer_products_2);
                    list2.setAdapter(new MainListAdapter(getView().getContext()));
                }
            });
        }
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_main;
    }

    private EzyerDataViewAdapter<TestFragmentViewHolder, NetStruct> mDataViewAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EzyerEntry.init(getActivity());

        mDataViewAdapter = new EzyerDataViewAdapter<TestFragmentViewHolder, NetStruct>(new TestFragmentViewHolder(getView())) {

            @Override
            protected void bindView(TestFragmentViewHolder holder, NetStruct user) {
                super.bindView(holder, user);


                bindView(holder.findViewById(R.id.customer_name), user.getData().get(0).getName());
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

        EzyerParseJsonRequest request = new EzyerParseJsonRequest("http://172.19.104.157:8001/SLive/index", new ResponseListener<NetStruct>() {
            @Override
            public void onResponse(Request<NetStruct> request, NetStruct response, boolean fromCache) {
                Log.d("MainFragment", "fromCache = " + fromCache);
                mDataViewAdapter.setData(response);
            }
        }, NetStruct.class);
        request.setTtl(30000).setSoftTtl(20000).setShouldCache(true);
        request.execute();
        
        
        
        
        
        
        
        
        
        
        
        
        
        
//        new JsonExecutor<>(JsonRequest.Method.GET, "http://172.19.104.157:8001/SLive/index", null, new ResponseListener<NetStruct>() {
//            @Override
//            public void onResponse(NetStruct response) {
//
//                Log.d("MainFragment", "response = " + response);
//
//                mDataViewAdapter.setData(response);
//            }
//        }, NetStruct.class).execute();

//        User user = new User();
//        user.setId(1);
//        user.setName("customer_name");
//
//        ItemList userItemList = new ItemList();
//        userItemList.setId("2");
//        userItemList.setName("item list for user:" + user.getName());
//
//        List<Item> itemList = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Item item = new Item();
//            item.setId(i);
//            item.setName("name" + i);
//            item.setImg1("http://static.googleadsserving.cn/pagead/imgad?id=CICAgKDT04GiTxCsAhj6ATIIdev4PTDcUv0");
//            item.setImg2("http://static.googleadsserving.cn/pagead/imgad?id=CICAgKDT04GiTxCsAhj6ATIIdev4PTDcUv0");
//            item.setImg3("http://static.googleadsserving.cn/pagead/imgad?id=CICAgKDT04GiTxCsAhj6ATIIdev4PTDcUv0");
//            itemList.add(item);
//        }
//        userItemList.setItemList(itemList);
//        user.setItemWrapper1(userItemList);
//
//        user.setItemWrapper2(userItemList);
//        mDataViewAdapter.setData(user);
    }
}
