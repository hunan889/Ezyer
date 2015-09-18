package com.niuan.common.ezyernet;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Carlos on 2015/9/18.
 */
public class EzyerJsonObjectRequest extends EzyerJsonRequest<JSONObject> {

    /**
     * Creates a new request.
     *
     * @param method      the HTTP method to use
     * @param url         URL to fetch the JSON from
     * @param requestBody A {@link String} to post with the request. Null is allowed and
     *                    indicates no parameters will be posted along with request.
     * @param listener    Listener to receive the JSON response
     */
    public EzyerJsonObjectRequest(int method, String url, String requestBody,
                                  ResponseListener<JSONObject> listener) {
        super(method, url, requestBody, listener);
    }

    /**
     * Creates a new request.
     *
     * @param url      URL to fetch the JSON from
     * @param listener Listener to receive the JSON response
     */
    public EzyerJsonObjectRequest(String url, ResponseListener<JSONObject> listener) {
        super(Method.GET, url, null, listener);
    }

    /**
     * Creates a new request.
     *
     * @param method   the HTTP method to use
     * @param url      URL to fetch the JSON from
     * @param listener Listener to receive the JSON response
     */
    public EzyerJsonObjectRequest(int method, String url, ResponseListener<JSONObject> listener) {
        super(method, url, null, listener);
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
    public EzyerJsonObjectRequest(int method, String url, JSONObject jsonRequest,
                                  ResponseListener<JSONObject> listener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener);
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see #EzyerJsonObjectRequest(int, String, JSONObject, ResponseListener)
     */
    public EzyerJsonObjectRequest(String url, JSONObject jsonRequest, ResponseListener<JSONObject> listener) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest, listener);
    }

    @Override
    protected JSONObject readResponse(NetworkResponse response) throws VolleyError {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return new JSONObject(jsonString);
        } catch (UnsupportedEncodingException e) {
            throw new ParseError(e);
        } catch (JSONException je) {
            throw new ParseError(je);
        }
    }
}