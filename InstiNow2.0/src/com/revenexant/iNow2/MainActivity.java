package com.revenexant.iNow2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
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
	private Button complaint;
    public static boolean check;
	 private static final String url = "http://students.iitm.ac.in/mobops_testing/thoughtcloud_registration.php";
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
	        	complaint = (Button) findViewById(R.id.complaintButton);
	        	complaint.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent com = new Intent(MainActivity.this,ComplaintActivity.class);
						startActivity(com);
						
					}
				});// end of Complaint button listener
	     }//end of finally
	}//end OnCreate
	 
	 private boolean NetCheck() {
		 boolean czech = false;
		 try{
			 czech = checkInternetConnection(MainActivity.this);
		 }catch(Exception e){Toast.makeText(MainActivity.this,"Connection check failed.",Toast.LENGTH_SHORT).show();
		 return false;}
		 if(czech){
			 			LoginAttempt la = new LoginAttempt();
			 			try{la.execute();}
			 			catch(Exception e){
			 				Log.e("ASync:",e.toString());//check for AsyncTask error
			 			}
		    	} else {
		    		     Toast.makeText(MainActivity.this,"No internet connection.",Toast.LENGTH_SHORT).show();
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
	     		//making sure the input is in Capital letters
	     		users.add(new BasicNameValuePair("roll", username1.getText().toString().toUpperCase()));
	     		users.add(new BasicNameValuePair("pass", password1.getText().toString()));
	     		try{
	     			success = new JsonParser().makeHttpRequest(url, "POST", users);
	     			//returns 1 if login is done, or 0 if not successful.
	     			Log.v("successint", "Now it's "+success);// check LogCat output.
	     		} catch(Exception e){
	     			Log.e("JSON", "JSON failed.");
	     		}
	     		
	     		
	     		if (success == 1) {
	     			logintest=true;
	     			Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
	     		}else{logintest=false;
	     			Toast.makeText(MainActivity.this,"Invalid credentials.",Toast.LENGTH_SHORT).show();
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

