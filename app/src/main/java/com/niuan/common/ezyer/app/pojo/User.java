package com.niuan.common.ezyer.app.pojo;

import com.niuan.common.ezyer.ui.annotation.EzyerData;
import com.niuan.common.ezyer.ui.annotation.EzyerDataType;

/**
 * Created by Carlos Liu on 2015/8/13.
 */
public class User {

    public static final int ID_NAME = 1;
    public static final int ID_USER_ID = 2;
    public static final int ID_ITEM_LIST_WRAPPER_1 = 3;
    public static final int ID_ITEM_LIST_WRAPPER_2 = 4;

    @EzyerData(id = ID_NAME)
    private String mName;

    @EzyerData(id = ID_USER_ID)
    private int mId;

    @EzyerData(id = ID_ITEM_LIST_WRAPPER_1, type = EzyerDataType.TYPE_WRAPPER)
    private ItemList mItemWrapper1;

    @EzyerData(id = ID_ITEM_LIST_WRAPPER_2, type = EzyerDataType.TYPE_WRAPPER)
    private ItemList mItemWrapper2;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public ItemList getItemWrapper1() {
        return mItemWrapper1;
    }

    public void setItemWrapper1(ItemList itemWrapper1) {
        mItemWrapper1 = itemWrapper1;
    }

    public ItemList getItemWrapper2() {
        return mItemWrapper2;
    }

    public void setItemWrapper2(ItemList itemWrapper2) {
        mItemWrapper2 = itemWrapper2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (mId != user.mId) return false;
        if (mName != null ? !mName.equals(user.mName) : user.mName != null) return false;
        if (mItemWrapper1 != null ? !mItemWrapper1.equals(user.mItemWrapper1) : user.mItemWrapper1 != null)
            return false;
        return !(mItemWrapper2 != null ? !mItemWrapper2.equals(user.mItemWrapper2) : user.mItemWrapper2 != null);

    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + mId;
        result = 31 * result + (mItemWrapper1 != null ? mItemWrapper1.hashCode() : 0);
        result = 31 * result + (mItemWrapper2 != null ? mItemWrapper2.hashCode() : 0);
        return result;
    }
}
