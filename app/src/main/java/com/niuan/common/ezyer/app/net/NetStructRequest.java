package com.niuan.common.ezyer.app.net;

import com.niuan.common.ezyer.app.pojo.NetStruct;

/**
 * Created by Carlos on 2015/9/20.
 */
public class NetStructRequest extends BaseRequest<NetStruct> {
    public NetStructRequest() {
        super(RequestUrlList.URL_NET_STRUCT, null, NetStruct.class);
    }
}
