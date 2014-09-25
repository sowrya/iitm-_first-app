package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity{
    private EditText username,password;
	private Button login;
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static boolean check;
	JsonParser jsonparser=new JsonParser();
	 private static final String url = "http://students.iitm.ac.in/mobops_testing/login.php";
	 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.loginpage);
			username=(EditText) findViewById(R.id.editText1);
			password=(EditText) findViewById(R.id.editText2);
			login=(Button) findViewById(R.id.button1);
			login.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//NetCheck();
					
				//please re-enable NetCheck later when logging in is possible.
					Intent sPoint = new Intent("com.revenexant.iNow2.WINDOWPOINT");
					startActivity(sPoint);
				}}
				);}
	 //ends this window when button is pressed.
	 @Override
	protected void onPause() {
			super.onPause();
			this.finish();
		}
	 
	 //Checking for internet connection in this.
	 private void NetCheck() 
	    {
		    	if (DetectConnection
		    		      .checkInternetConnection(MainActivity.this)) {
		    		     new AttemptLogin().execute();
		    		    } else {
		    		     Toast.makeText(MainActivity.this,
		    		       "You Do not have Internet Connection",
		    		       Toast.LENGTH_LONG).show();
		    		 
		    		     MainActivity.this.startActivity(new Intent(
		    		       Settings.ACTION_WIRELESS_SETTINGS));
		    		    }
	        
			
			
	    }
			class AttemptLogin extends AsyncTask<Object, Object, Object> {
		        boolean failure = false;
				private ProgressDialog pDialog;
		        
		        protected void onPreExecute(String file_url) {
		        	super.onPreExecute();
		            pDialog = new ProgressDialog(MainActivity.this);
		            pDialog.setTitle("Contacting Servers");
		            pDialog.setMessage("Logging in ...");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
		        }

				@Override
				protected Object doInBackground(Object... params) {
					int success;
		            String username2 = username.getText().toString();
		            String password2= password.getText().toString();
		            try {
		                List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
		                users.add(new BasicNameValuePair("username3", username2));
		                users.add(new BasicNameValuePair("password3", password2));

		                Log.d("request!", "starting");
		                JSONObject json = jsonparser.makeHttpRequest(
		                       url, "POST", users);
		                success = json.getInt(TAG_SUCCESS);
		                if (success == 1) {
		                    Log.d("Login Successful!", json.toString());
		                    
		                    return json.getString(TAG_MESSAGE);
		                }else{
		                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
		                    return json.getString(TAG_MESSAGE);      
		                }
		            } catch (JSONException e) {
		                e.printStackTrace();
		            }				return null;
				}      
				protected void onPostExecute (
						String result){
					if(result.equals("Login successful!")){
						Intent i = new Intent(MainActivity.this, NavigationDrawerFragment.class);
	                    finish();
	                    startActivity(i);
					}
					else{
						Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					}
				}
		    }

}
