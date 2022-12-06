package com.macsoftech.ekart.api;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class VendorProductBySize {

    public static void getVendorProductBySize(Context context,final DadiVolleyCallback callback) {
        BaseAPI.getVendorProductBySize(context, new BaseAPI.DadiVolleyCallback() {
            @Override
            public String onSuccess(String result) {
                System.out.println("GetallUserdata: " + result);
                String disvalstring = "555";
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    if (jsonObject.has("statusCode")) {
                        callback.onSuccess("userservice"+jsonObject.toString());
                        //  ShowAlert.showAlertDialogButtonClicked(AccountID, jsonObject.toString());
                        String code = jsonObject.getString("statusCode");
                        if (code.equals("200")){
                            disvalstring ="Sucess"+ jsonObject.getString("Data");
                        }else{
                            disvalstring = "fail";
                        }
                        System.out.println("Userdata..******** " + disvalstring.toString());
                    } else {
                       // Toast.makeText(context, "Please try again..!! ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return callback.onSuccess(disvalstring);
            }

            @Override
            public String onFailure(String result) {
                return callback.onSuccess(result);
            }
        });
    }

    public interface DadiVolleyCallback {
        String onSuccess(String result);

        String onFailure(String result);
    }

}
