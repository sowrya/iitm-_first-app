package com.revenexant.iNow2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String jsonstr = "0";
    StringBuilder sb;

    // constructor
    public JsonParser() {

    }

  
    public String makeHttpRequest(String url, String method,List<BasicNameValuePair> params){
		 
        try {

            // check for request method. Standard stuff
            if(method.compareTo("POST")==0){
                // request method is POST
                // defaultHttpClient
            	
                DefaultHttpClient httpClient = null;
				HttpPost httpPost = null;
				try {
					httpClient = new DefaultHttpClient();
					httpPost = new HttpPost(url);
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				} catch (Exception e1) {
					Log.v("Part 1", e1.toString());
				}

                HttpResponse httpResponse = null;
				try {
					httpResponse = httpClient.execute(httpPost);
				} catch (Exception e2) {
					Log.v("No response", e2.toString());
				}
                HttpEntity httpEntity = null;
				try {
					httpEntity = httpResponse.getEntity();
				} catch (Exception e3) {
					Log.v("No entity", e3.toString());
				}
                try {
					is = httpEntity.getContent();
				} catch (Exception e4) {
					Log.v("No content", e4.toString());
				}

            }else if(method.compareTo("GET")==0){
                // request method is GET
            	URLConnection urlConnection = new URL(url).openConnection();
				is = urlConnection.getInputStream();
            }           

        } catch (Exception e) {
            Log.e("makeHttpRequest", e.toString());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
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
    
    public JSONObject returnJson(){
    	JSONObject p = null;
    	try {
			p =  new JSONObject(sb.toString());
		} catch (JSONException e) {
			Log.e("returnJson", "Cup");
		}
    	return p;
    }

	
}
