package com.macsoftech.ekart.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaseAPI {
    static RequestQueue queue;
    static String res;

    public static void getVendorProductBySize(Context context,
                                        final DadiVolleyCallback callback) {

        Cache cache = new DiskBasedCache(context.getCacheDir(), 10 * 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        queue = new RequestQueue(cache, network);
        // Start the queue
        queue.start();
        String deliverytype = "";


        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("size", "2");


        String url = "http://43.204.146.144:4000/admin-product/getVendorProductBySize?";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Reponse here " + response.toString());
                        callback.onSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error ms..." + error.getMessage());
                callback.onFailure("fail");
            }
        });
        System.out.println("Response url get Providers " + jsonObjectRequest.getUrl()+" params "+new JSONObject(postParam).toString());
        queue.add(jsonObjectRequest);
        // return res;
    }



    public interface DadiVolleyCallback {
        String onSuccess(String result);

        String onFailure(String result);
    }
}
