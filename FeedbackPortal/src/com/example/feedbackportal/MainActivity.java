package com.example.feedbackportal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private TextView username,password;
    private EditText username1,password1;
	private Button login;
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static boolean check;
	JsonParser jsonparser=new JsonParser();
	 private static final String url = "http://students.iitm.ac.in/mobops_testing/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(TextView) findViewById(R.id.textView1);
		password=(TextView) findViewById(R.id.textView2);
		username1=(EditText) findViewById(R.id.editText1);
		password1=(EditText) findViewById(R.id.editText2);
		login=(Button) findViewById(R.id.button1);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NetCheck();
			}}
			);}
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
	            String username2 = username1.getText().toString();
	            String password2= password1.getText().toString();
	            try {
	                List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
	                users.add(new BasicNameValuePair("username3", username2));
	                users.add(new BasicNameValuePair("password3", password2));

	                Log.d("request!", "starting");
	                JSONObject json = ((JsonParser) jsonparser).makeHttpRequest(
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
					Intent i = new Intent(MainActivity.this, Home.class);
                    finish();
                    startActivity(i);
				}
				else{
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				}
			}
	    }
    


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
}
