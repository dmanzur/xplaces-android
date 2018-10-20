package dmanzur.xplaces.Model.network;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;
import dmanzur.xplaces.view.LogoutAndAboutFragment;

public class UserHandler {


    private String baseUrl;


    public UserHandler(){
        String server = MyApplication.getAppContext().getResources().getString(R.string.serverRoot);
        this.baseUrl = server + "users/";
    }


    public void registerUser(String name, String email, String password,final MainActivity.IRegistrationListener callback){

        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        String address = this.baseUrl + "register";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Log.d("tag ", error.toString());
                callback.onComplete(false);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        callback.onComplete(true);
                    } catch (UnsupportedEncodingException e) {
                        callback.onComplete(false);
                    }
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }

    public void loginUser(String email, String password, final MainActivity.ILoginListener callback) {

        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        String address = this.baseUrl + "login";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Log.d("tag ","ErrorListener" +  error.toString());
                callback.onComplete(false);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        callback.onComplete(true);
                    } catch (UnsupportedEncodingException e) {
                        parsed = new String(response.data);
                        callback.onComplete(false);
                    }
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }


    public void logoutUser(final LogoutAndAboutFragment.ILogout callback){
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        String address = this.baseUrl + "logout";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Log.d("tag ", error.toString());
                callback.onComplete(false);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    try {
                        String parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        Log.d("tag ", "logout parsed: " + parsed);
                        callback.onComplete(true);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        callback.onComplete(false);
                    }
                } else {
                    callback.onComplete(false);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);

    }

}
