package dmanzur.xplaces.Model.network;

import android.util.Log;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;

public class PlacesHandler {

    private final String baseUrl;

    public PlacesHandler() {
        String server = MyApplication.getAppContext().getResources().getString(R.string.serverRoot);
        this.baseUrl = server + "places/";
    }

    public void getPlaceDescription(String name, final PlacesService.IPlaceDescription callback) {
        String dandelionApiKey = MyApplication.getAppContext().getResources().getString(R.string.dandelionAPI);

        try {
            String encodedPlaceName = URLEncoder.encode(name, "UTF-8");
            RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
            String address = "https://api.dandelion.eu/datatxt/nex/v1/?lang=en&text=" + encodedPlaceName + "&include=abstract&token=" + dandelionApiKey;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, address, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                public void onErrorResponse(com.android.volley.error.VolleyError error) {
                    callback.onError(error.getMessage());
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
                        responseString = String.valueOf(response.statusCode);
                        String parsed;
                        try {
                            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            try {
                                Log.d("tag", "description found");
                                JSONObject jsonObj = new JSONObject(parsed);
                                JSONArray annotations = jsonObj.getJSONArray("annotations");
                                if (annotations.length() > 0) {
                                    JSONObject first = annotations.getJSONObject(0);
                                    String description = first.getString("abstract");
                                    Log.d("tag", "description done");
                                    callback.onComplete(description);
                                } else {
                                    callback.onComplete("no description found");
                                }
                            } catch (JSONException e) {
                                Log.d("tag", "JSONException");
                                e.printStackTrace();
                                callback.onError(e.getMessage());
                            }
                        } catch (UnsupportedEncodingException e) {
                            Log.d("tag", "UnsupportedEncodingException");
                            callback.onError(e.getMessage());
                        }
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }


    public void getPlacesListIDs(final PlacesService.IPlacesListIdsListener callback) {

        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Log.d("tag ", error.toString());
                callback.onError(error.toString());
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
                    responseString = String.valueOf(response.statusCode);
                    String parsed;
                    try {
                        parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        List<String> placesIds = new ArrayList<String>();
                        try {
                            JSONArray jsonArray = new JSONArray(parsed);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String placeId = jsonArray.getJSONObject(i).getString("placeId");
                                placesIds.add(placeId);
                                Log.d("tag ", "placeId returned: " + placeId);
                            }
                            callback.onComplete(placesIds);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError(e.getMessage());
                        }
                    } catch (UnsupportedEncodingException e) {
                        callback.onError(e.getMessage());
                    }
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        //stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    public void findImage(final String imageUriPath, final PlacesService.IPlacesServiceSearch callback) {

        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        String address = this.baseUrl + "find";
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String message = jObj.getString("value");
                            Log.d("tag", "complete find");
                            callback.onComplete(message);
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            callback.onError("error find image");
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(com.android.volley.error.VolleyError error) {
                Log.d("onErrorResponse ", error.getMessage());
                callback.onError(error.getMessage());

            }
        });

        Log.d("tag", "img path: " + imageUriPath);
        smr.addFile("img", imageUriPath);
        requestQueue.add(smr);
    }
}
