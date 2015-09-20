package com.niuan.common.ezyer.app.pojo;

import com.niuan.common.ezyer.ui.annotation.EzyerData;

public class Dish {

    public static final int ID_IMG = 1;
    public static final int ID_NAME = 2;
    public static final int ID_AUTHOR = 3;
    public static final int ID_METHOD = 4;
    public static final int ID_TIME = 5;

    @EzyerData(id = ID_IMG)
    public String image;

    public String detailUrl;

    @EzyerData(id = ID_NAME)
    public String name;

    @EzyerData(id = ID_AUTHOR)
    public String author;

    @EzyerData(id = ID_METHOD)
    public String method;

    @EzyerData(id = ID_TIME)
    public String time;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dish dish = (Dish) o;

        if (image != null ? !image.equals(dish.image) : dish.image != null) return false;
        if (detailUrl != null ? !detailUrl.equals(dish.detailUrl) : dish.detailUrl != null)
            return false;
        if (name != null ? !name.equals(dish.name) : dish.name != null) return false;
        if (author != null ? !author.equals(dish.author) : dish.author != null) return false;
        if (method != null ? !method.equals(dish.method) : dish.method != null) return false;
        return !(time != null ? !time.equals(dish.time) : dish.time != null);

    }

    @Override
    public int hashCode() {
        int result = image != null ? image.hashCode() : 0;
        result = 31 * result + (detailUrl != null ? detailUrl.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"author\":\"").append(author).append('\"');
        sb.append(", \"image\":\"").append(image).append('\"');
        sb.append(", \"detailUrl\":\"").append(detailUrl).append('\"');
        sb.append(", \"name\":\"").append(name).append('\"');
        sb.append(", \"method\":\"").append(method).append('\"');
        sb.append(", \"time\":\"").append(time).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
