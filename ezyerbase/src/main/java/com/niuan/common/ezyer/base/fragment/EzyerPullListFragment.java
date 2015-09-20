//package com.niuan.common.ezyer.base.fragment;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.ListView;
//
//import com.niuan.common.ezyer.ui.view.adapter.EzyerBaseListAdapter;
//import com.niuan.common.ezyer.ui.view.adapter.EzyerDataViewAdapter;
//import com.niuan.common.ezyer.ui.view.holder.EzyerViewHolder;
//
//import java.util.ArrayList;
//
///**
// * Created by Carlos Liu on 2015/9/19.
// */
//public abstract class EzyerPullListFragment<T extends EzyerViewHolder, DATA> extends EzyerPullViewFragment<T>
//        implements AbsListView.OnScrollListener {
//
//    private ListView mListView;
//    private EzyerBaseListAdapter<DATA> mListAdapter;
//    private EzyerDataViewAdapter<T, ResultWrapper<DATA>> mDataViewAdapter;
//
//    public static final int TYPE_REFRESH = 1;
//    public static final int TYPE_LOAD = 2;
//    public static final int TYPE_INIT = 3;
//
//    private static final class ResultWrapper<DATA> {
//        public final int mType;
//        public final ArrayList<DATA> mDataList;
//
//        public ResultWrapper(ArrayList<DATA> dataList, int type) {
//            mDataList = dataList;
//            mType = type;
//        }
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mListAdapter = initListAdapter();
//        mListView = findViewById(getListViewId());
//        mListView.setAdapter(mListAdapter);
//        mListView.setOnScrollListener(this);
//        mDataViewAdapter = new EzyerDataViewAdapter<T, ResultWrapper<DATA>>(getViewHolder()) {
//            @Override
//            protected void bindView(T holder, ResultWrapper<DATA> dataResultWrapper) {
//                int type = dataResultWrapper.mType;
//                switch (type) {
//                    case TYPE_INIT: {
//                        mListAdapter.setDataSource(dataResultWrapper.mDataList);
//                        break;
//                    }
//                    case TYPE_LOAD: {
//                        mListAdapter.addDataSource(dataResultWrapper.mDataList);
//                        break;
//                    }
//                    case TYPE_REFRESH: {
//                        mListAdapter.addDataSourceFront(dataResultWrapper.mDataList);
//                        break;
//                    }
//                }
//            }
//        };
//    }
//
//    protected abstract EzyerBaseListAdapter<DATA> initListAdapter();
//
//    protected EzyerBaseListAdapter<DATA> getListAdapter() {
//        return mListAdapter;
//    }
//
//    protected int getListViewId() {
//        return 0;
//    }
//
//    protected void onLoad() {
//
//    }
//
//    protected void requestFinish(int type, ArrayList<DATA> list) {
//        mDataViewAdapter.bindData(new ResultWrapper<>(list, type));
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//    }
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//}
