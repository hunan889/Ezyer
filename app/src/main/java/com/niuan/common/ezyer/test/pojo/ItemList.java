package com.niuan.common.ezyer.test.pojo;

import com.niuan.common.ezyer.base.annotation.EzyerData;

import java.util.List;

/**
 * Created by Carlos Liu on 2015/8/13.
 */
public class ItemList {
    
    public static final int ID_LIST_ID = 1;
    public static final int ID_LIST_NAME = 2;
    public static final int ID_LIST_LIST = 3;

    @EzyerData(id = ID_LIST_ID)
    private String mId;

    @EzyerData(id = ID_LIST_NAME)
    private String name;

    @EzyerData(id = ID_LIST_LIST)
    private List<Item> mItemList;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public List<Item> getItemList() {
        return mItemList;
    }

    public void setItemList(List<Item> itemList) {
        mItemList = itemList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemList itemList = (ItemList) o;

        if (mId != null ? !mId.equals(itemList.mId) : itemList.mId != null) return false;
        if (name != null ? !name.equals(itemList.name) : itemList.name != null) return false;
        return !(mItemList != null ? !mItemList.equals(itemList.mItemList) : itemList.mItemList != null);

    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (mItemList != null ? mItemList.hashCode() : 0);
        return result;
    }
}
