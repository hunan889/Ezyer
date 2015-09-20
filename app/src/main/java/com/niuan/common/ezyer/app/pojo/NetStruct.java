package com.niuan.common.ezyer.app.pojo;

import com.niuan.common.ezyer.ui.annotation.EzyerData;

import java.util.List;

/**
 * Created by Carlos on 2015/9/17.
 */
public class NetStruct {

    public static final int ID_CODE = 1;
    public static final int ID_MSG = 2;
    public static final int ID_LIST = 3;
    public static final int ID_LIST_NAME = 4;
    public static final int ID_LIST_PAGE_SIZE = 5;
    public static final int ID_LIST_PAGE_ACTION = 6;
    public static final int ID_LIST_ICON = 7;

    @EzyerData(id = ID_CODE)
    private int code;

    @EzyerData(id = ID_MSG)
    private String message;

    @EzyerData(id = ID_LIST)
    private List<Data> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        @EzyerData(id = ID_LIST_NAME)
        private String name;
        @EzyerData(id = ID_LIST_PAGE_SIZE)
        private int page_size;
        @EzyerData(id = ID_LIST_PAGE_ACTION)
        private String action;
        @EzyerData(id = ID_LIST_ICON)
        private String icon;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPage_size() {
            return page_size;
        }

        public void setPage_size(int page_size) {
            this.page_size = page_size;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"action\":\"").append(action).append('\"');
            sb.append(", \"name\":\"").append(name).append('\"');
            sb.append(", \"page_size\":").append(page_size);
            sb.append(", \"icon\":\"").append(icon).append('\"');
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":").append(code);
        sb.append(", \"message\":\"").append(message).append('\"');
        sb.append(", \"data\":").append(data);
        sb.append('}');
        return sb.toString();
    }
}
