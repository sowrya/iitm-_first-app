package com.revenexant.iNow2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity{
	private static final String sett = "mtiiops";
	SharedPreferences save;
    private EditText username1, password1;
    private static boolean logintest = false;
	private Button login;
    public static boolean check;
	 private static final String url = "http://students.iitm.ac.in/mobops_testing/login.php";
	 //from JSONParser

	    static InputStream is = null;
	    static JSONObject jObj = null;
	    static String jsonstr = "";
	 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        try{
	        save = getSharedPreferences(sett,0);
	        if(save.getBoolean("loggedin", false)){
	        	Intent i = new Intent(MainActivity.this, UserChoices.class);
                startActivity(i);}
	        }catch(Exception e){}
	        finally{
	        	setContentView(R.layout.loginpage);
	        	username1=(EditText) findViewById(R.id.editText1);
	        	password1=(EditText) findViewById(R.id.editText2);
	        	login=(Button) findViewById(R.id.button1);
	        	login.setOnClickListener(new OnClickListener() {
				
	        		@Override
				public void onClick(View v) {
					//check connectivity
					if(NetCheck()){
						if(logintest){
							//save to preferences
							save = getSharedPreferences(sett,0);
							SharedPreferences.Editor editor = save.edit();
						    editor.putBoolean("loggedin", true);
						    editor.putString("username", username1.getText().toString());
						    editor.commit();
						    Intent i = new Intent(MainActivity.this,UserChoices.class);
							startActivity(i);
						}// if login test
					}//if net check
				}
			});//end OnClickListener
	     }//end of finally
	}//end OnCreate
	 
	 private boolean NetCheck() {
		 boolean czech = false;
		 try{
			 czech = checkInternetConnection(MainActivity.this);
		 }catch(Exception e){Toast.makeText(MainActivity.this,"Connection check failed.",Toast.LENGTH_LONG).show();
		 return false;}
		 if(czech){
			 			LoginAttempt la = new LoginAttempt();
			 			try{la.execute();}
			 			catch(Exception e){
			 				Log.e("ASync:",e.toString());
			 			}
		    	} else {
		    		     Toast.makeText(MainActivity.this,"No internet connection.",Toast.LENGTH_LONG).show();
		    		    }
		 return czech;
		   }//end of NetCheck
	 
	 private class LoginAttempt extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int success=0;
	     	try {
	     		Looper.prepare();
	     		List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
	     		users.add(new BasicNameValuePair("username", username1.getText().toString()));
	     		users.add(new BasicNameValuePair("password", password1.getText().toString()));
	     		try{
	     			JSONObject json = makeHttpRequest(url, "POST", users);
	     			success = json.getInt("success");
	     			Log.v("successint", "Now it's "+success);
	     		} catch(Exception e){
	     			Log.e("JSON", "JSON failed.");
	     		}
	     		
	     		
	     		if (success == 1) {
	     			logintest=true;
	     			Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
	     		}else{logintest=false;
	     			Toast.makeText(MainActivity.this,"Invalid credentials.",Toast.LENGTH_LONG).show();
	     			jsonstr = "";
	     		}
	     		Looper.loop();
	     	} catch (Exception e) {
	     		Log.e("doInBackground", "failed.  "+e.toString());
	     	}
			return null;
		}//end of do in background

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
		
		
		 
	 }// end of ASyncTask
	 
	 public JSONObject makeHttpRequest(String url, String method,List params) {
		 
	        try {

	            // check for request method
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
	        try {
	            jObj = new JSONObject(jsonstr);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data.");
	        }

	        // return JSON String
	        return jObj;

	    }// end of makeHttpRequest. This is from JSONParser.

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        if (id == R.id.action_settings) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	    
	    public static boolean checkInternetConnection(Context context) {
	    	 
	    	  ConnectivityManager con_manager = (ConnectivityManager) context
	    	    .getSystemService(Context.CONNECTIVITY_SERVICE);
	    	 
	    	  if (con_manager.getActiveNetworkInfo() != null
	    	    && con_manager.getActiveNetworkInfo().isAvailable()
	    	    && con_manager.getActiveNetworkInfo().isConnected()) {
	    	   return true;} else {return false;}
	    	}
	   
	    @Override
		protected void onPause() {
			super.onPause();
			this.finish();
		}
		@Override
		protected void onStop() {
			super.onStop();
		}
	    
	    
	}

