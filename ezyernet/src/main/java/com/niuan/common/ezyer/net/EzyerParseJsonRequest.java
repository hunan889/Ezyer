package com.niuan.common.ezyer.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Carlos on 2015/9/18.
 */
public class EzyerParseJsonRequest<T> extends EzyerJsonRequest<T> {

    private Type mParseType;

    /**
     * Creates a new request.
     *
     * @param method      the HTTP method to use
     * @param url         URL to fetch the JSON from
     * @param requestBody A {@link String} to post with the request. Null is allowed and
     *                    indicates no parameters will be posted along with request.
     */
    public EzyerParseJsonRequest(int method, String url, String requestBody, Type parseType) {
        super(method, url, requestBody);
        mParseType = parseType;
    }

    /**
     * Creates a new request.
     *
     * @param url URL to fetch the JSON from
     */
    public EzyerParseJsonRequest(String url, Type parseType) {
        this(Method.GET, url, (String) null, parseType);
    }

    /**
     * Creates a new request.
     *
     * @param method the HTTP method to use
     * @param url    URL to fetch the JSON from
     */
    public EzyerParseJsonRequest(int method, String url, Type parseType) {
        this(method, url, (String) null, parseType);
    }

    /**
     * Creates a new request.
     *
     * @param method      the HTTP method to use
     * @param url         URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *                    indicates no parameters will be posted along with request.
     */
    public EzyerParseJsonRequest(int method, String url, JSONObject jsonRequest, Type parseType) {
        this(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), parseType);
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see #EzyerParseJsonRequest(int, String, JSONObject, Type)
     */
    public EzyerParseJsonRequest(String url, JSONObject jsonRequest, Type parseType) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest, parseType);
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see #EzyerParseJsonRequest(int, String, JSONObject, Type)
     */
    public EzyerParseJsonRequest(String url, String requestBody, Type parseType) {
        this(requestBody == null ? Method.GET : Method.POST, url, requestBody, parseType);
    }

    @Override
    protected T readResponse(NetworkResponse response) throws VolleyError {
        return readResponse(response.data, response.headers);
    }

    protected T readResponse(byte[] data, Map<String, String> headers) throws VolleyError {
        try {
            String jsonString = new String(data,
                    HttpHeaderParser.parseCharset(headers, PROTOCOL_CHARSET));
            return JsonParser.parseJson(jsonString, mParseType);
        } catch (UnsupportedEncodingException e) {
            throw new ParseError(e);
        } catch (JsonSyntaxException je) {
            throw new ParseError(je);
        }
    }

    protected byte[] getBytes(T object) {
        String json = JsonParser.toJson(object, mParseType);
        try {
            return json.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}