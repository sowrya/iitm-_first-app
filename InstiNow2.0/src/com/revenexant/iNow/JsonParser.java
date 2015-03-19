package com.revenexant.iNow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
            	try {
            		 UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
            		 URL ul = new URL(url);
            		 HttpsURLConnection request = (HttpsURLConnection) ul.openConnection();

            		 request.setUseCaches(false);
            		 request.setDoOutput(true);
            		 request.setDoInput(true);

            		 request.setRequestMethod("POST");
            		 OutputStream post = request.getOutputStream();
            		 entity.writeTo(post);
            		 post.flush();
            		 is = request.getInputStream();
            		} catch (Exception e) {
            		 Log.e("Your app", e.toString());
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
