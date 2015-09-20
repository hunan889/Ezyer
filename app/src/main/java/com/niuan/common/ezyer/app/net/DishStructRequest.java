package com.niuan.common.ezyer.app.net;

import com.google.gson.reflect.TypeToken;
import com.niuan.common.ezyer.app.pojo.Dish;
import com.niuan.common.ezyer.app.pojo.NetStruct;

import java.util.ArrayList;

/**
 * Created by Carlos on 2015/9/20.
 */
public class DishStructRequest extends BaseRequest<ArrayList<Dish>> {
    public DishStructRequest() {
        super(RequestUrlList.URL_DISH_LIST, null, new TypeToken<ArrayList<Dish>>() {
        }.getType());
    }
}
