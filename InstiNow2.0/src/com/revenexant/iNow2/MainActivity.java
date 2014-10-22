package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private EditText username1,password1;
    private static int test;
	private Button login;
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static boolean check;
	JsonParser jsonparser = new JsonParser();
	 private static final String url = "http://students.iitm.ac.in/mobops_testing/login.php";
	 
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
					//save to preferences
					save = getSharedPreferences(sett,0);
					SharedPreferences.Editor editor = save.edit();
				      editor.putBoolean("loggedin", true);
				      editor.putString("username", username1.getText().toString());
				      editor.commit();}
					else{
						Intent i = new Intent(MainActivity.this,UserChoices.class);
						startActivity(i);
					}
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
			 		try{Toast.makeText(MainActivity.this,"Logged in.",Toast.LENGTH_LONG).show();
			 			//AttemptLogin loggy = new AttemptLogin();
			 			//loggy.execute();
			 			//get rid of comments when repaired AttemptLogin
			 			}
			 		catch(Exception e){
			 			Toast.makeText(MainActivity.this,"Attempt login failed.",Toast.LENGTH_LONG).show();
			 			czech = false;}
		    	} else {
		    		     Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
		    		    }
		 return czech;
		   }
	 
	 class AttemptLogin extends AsyncTask<String, String, String> {
			private ProgressDialog pDialog;
	        @Override
	        protected void onPreExecute() {
	        	super.onPreExecute();
	            pDialog = new ProgressDialog(MainActivity.this);
	            pDialog.setMessage("Logging in ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

	        @Override
	        protected String doInBackground(String... params) {
	        	int success;
	        	String username2 = username1.getText().toString();
	        	String password2= password1.getText().toString();
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
	        			test=1;
	        			return json.getString(TAG_MESSAGE);
	        		}else{
	        			Log.d("Login Failure!", json.getString(TAG_MESSAGE));
	        			return json.getString(TAG_MESSAGE);      
	        		}
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        		Toast.makeText(getApplicationContext(), "Background error.", Toast.LENGTH_LONG).show();
	        	}
	        	return null;
	        }
			@Override
			protected void onPostExecute (String file_url){
				pDialog.dismiss();
				if(test==1){
					Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
					Intent i = new Intent(MainActivity.this, UserChoices.class);
                 startActivity(i);
				}
				else{
					Toast.makeText(getApplicationContext(), "Invalid Credentials" , Toast.LENGTH_LONG).show();
					Intent i = new Intent(MainActivity.this,MainActivity.class);
                 startActivity(i);
				}
			}
			
	 	}// end of AttemtLogin class

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

