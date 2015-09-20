package com.niuan.common.ezyer.app.pojo;

import com.niuan.common.ezyer.ui.annotation.EzyerData;

/**
 * Created by Carlos Liu on 2015/8/13.
 */
public class Item {
    public static final int ID_ITEM_NAME = 1;
    public static final int ID_ITEM_ID = 2;
    public static final int ID_ITEM_IMG1 = 3;
    public static final int ID_ITEM_IMG2 = 4;
    public static final int ID_ITEM_IMG3 = 5;

    @EzyerData(id = ID_ITEM_NAME)
    private String mName;

    @EzyerData(id = ID_ITEM_ID)
    private int mId;

    @EzyerData(id = ID_ITEM_IMG1)
    private String mImg1;

    @EzyerData(id = ID_ITEM_IMG2)
    private String mImg2;

    @EzyerData(id = ID_ITEM_IMG3)
    private String mImg3;

    public String getImg1() {
        return mImg1;
    }

    public void setImg1(String img1) {
        mImg1 = img1;
    }

    public String getImg2() {
        return mImg2;
    }

    public void setImg2(String img2) {
        mImg2 = img2;
    }

    public String getImg3() {
        return mImg3;
    }

    public void setImg3(String img3) {
        mImg3 = img3;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (mId != item.mId) return false;
        if (mName != null ? !mName.equals(item.mName) : item.mName != null) return false;
        if (mImg1 != null ? !mImg1.equals(item.mImg1) : item.mImg1 != null) return false;
        if (mImg2 != null ? !mImg2.equals(item.mImg2) : item.mImg2 != null) return false;
        return !(mImg3 != null ? !mImg3.equals(item.mImg3) : item.mImg3 != null);

    }

    @Override
    public int hashCode() {
        int result = mName != null ? mName.hashCode() : 0;
        result = 31 * result + mId;
        result = 31 * result + (mImg1 != null ? mImg1.hashCode() : 0);
        result = 31 * result + (mImg2 != null ? mImg2.hashCode() : 0);
        result = 31 * result + (mImg3 != null ? mImg3.hashCode() : 0);
        return result;
    }
}
