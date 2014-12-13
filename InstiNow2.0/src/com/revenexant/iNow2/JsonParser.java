package com.revenexant.iNow2;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String jsonstr = "0";

    // constructor
    public JsonParser() {

    }

  
    public String makeHttpRequest(String url, String method,List<BasicNameValuePair> params) {
		 
        try {

            // check for request method. Standard stuff
            if(method.compareTo("POST")==0){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if(method.compareTo("GET")==0){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }           

        } catch (Exception e) {
            Log.e("makeHttpRequest", e.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonstr = sb.toString();
            Log.v("StringBuilder", jsonstr);
            
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        
        return jsonstr;
    }
    
    public boolean checkInternetConnection(Context context) {
   	 
  	  ConnectivityManager con_manager = (ConnectivityManager) context
  	    .getSystemService(Context.CONNECTIVITY_SERVICE);
  	 
  	  if (con_manager.getActiveNetworkInfo() != null
  	    && con_manager.getActiveNetworkInfo().isAvailable()
  	    && con_manager.getActiveNetworkInfo().isConnected()) {
  	   return true;} else {return false;}
  	}

	
}
