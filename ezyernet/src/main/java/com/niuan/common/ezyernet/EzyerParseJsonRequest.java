package com.niuan.common.ezyernet;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Carlos on 2015/9/18.
 */
public class EzyerParseJsonRequest<T> extends EzyerJsonRequest<T> {

    private Class<T> mParseType;

    /**
     * Creates a new request.
     *
     * @param method      the HTTP method to use
     * @param url         URL to fetch the JSON from
     * @param requestBody A {@link String} to post with the request. Null is allowed and
     *                    indicates no parameters will be posted along with request.
     * @param listener    Listener to receive the JSON response
     */
    public EzyerParseJsonRequest(int method, String url, String requestBody,
                                 ResponseListener<T> listener, Class<T> parseType) {
        super(method, url, requestBody, listener);
        mParseType = parseType;
    }

    /**
     * Creates a new request.
     *
     * @param url      URL to fetch the JSON from
     * @param listener Listener to receive the JSON response
     */
    public EzyerParseJsonRequest(String url, ResponseListener<T> listener, Class<T> parseType) {
        this(Method.GET, url, (String) null, listener, parseType);
    }

    /**
     * Creates a new request.
     *
     * @param method   the HTTP method to use
     * @param url      URL to fetch the JSON from
     * @param listener Listener to receive the JSON response
     */
    public EzyerParseJsonRequest(int method, String url, ResponseListener<T> listener, Class<T> parseType) {
        this(method, url, (String) null, listener, parseType);
    }

    /**
     * Creates a new request.
     *
     * @param method      the HTTP method to use
     * @param url         URL to fetch the JSON from
     * @param jsonRequest A {@link JSONObject} to post with the request. Null is allowed and
     *                    indicates no parameters will be posted along with request.
     * @param listener    Listener to receive the JSON response
     */
    public EzyerParseJsonRequest(int method, String url, JSONObject jsonRequest,
                                 ResponseListener<T> listener, Class<T> parseType) {
        this(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, parseType);
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see #EzyerParseJsonRequest(int, String, JSONObject, ResponseListener, Class)
     */
    public EzyerParseJsonRequest(String url, JSONObject jsonRequest, ResponseListener<T> listener, Class<T> parseType) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest, listener, parseType);
    }

    @Override
    protected T readResponse(NetworkResponse response) throws VolleyError {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return JsonParser.parseJson(jsonString, mParseType);
        } catch (UnsupportedEncodingException e) {
            throw new ParseError(e);
        } catch (JsonSyntaxException je) {
            throw new ParseError(je);
        }
    }
}