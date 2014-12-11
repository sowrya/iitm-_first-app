package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.annotation.SuppressLint;
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
	private Button login;
	private Button loginCheat;
    public static boolean check;
	private static final String url = "http://students.iitm.ac.in/mobops_testing/login.php";
	private String name = "";
	static JSONObject jObj = null;
	static String jsonstr = "";
	 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        try{
	        save = getSharedPreferences(sett,0);
	        if(save.contains("loggedin")){
	        	Intent i = new Intent(MainActivity.this, UserChoices.class);
                startActivity(i);}
	        }catch(Exception e){Log.e("SharedPref", "Error creating SPs.");}
	        finally{
	        	setContentView(R.layout.loginpage);
	        	username1=(EditText) findViewById(R.id.editText1);
	        	password1=(EditText) findViewById(R.id.editText2);
	        	login=(Button) findViewById(R.id.button1);
	        	login.setOnClickListener(new OnClickListener() {
				
	        		@Override
				public void onClick(View v) {
					//check connectivity
					NetCheck();
				}
			});//end OnClickListener
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

		@SuppressLint("DefaultLocale")
		@Override
		protected Void doInBackground(Void... params) {
			int success=0;
	     	try {
	     		Looper.prepare();
	     		List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
	     		//making sure the input is in Capital letters
	     		name = username1.getText().toString().toUpperCase();
	     		users.add(new BasicNameValuePair("roll", name));
	     		users.add(new BasicNameValuePair("pass", password1.getText().toString()));
	     		try{
	     			jsonstr = new JsonParser().makeHttpRequest(url, "POST", users);
	     			jObj = new JSONObject(jsonstr);
	     			success = jObj.getInt("success");
	     			Log.v("successint", "Now it's "+success);// check LogCat output.
	     			Log.v("message", jObj.getString("message"));
	     		} catch(Exception e){
	     			Log.e("JSON", "JSON failed.");
	     		}
	     		
	     		if (success == 1) {
	     			Toast.makeText(MainActivity.this,
	     					"Welcome "+name+".",Toast.LENGTH_SHORT).show();
	     			save = getSharedPreferences(sett,0);
					SharedPreferences.Editor editor = save.edit();
				    editor.putBoolean("loggedin", true);
				    editor.putString("username", username1.getText().toString());
				    editor.commit();
				    Intent i = new Intent(MainActivity.this,UserChoices.class);
					startActivity(i);
	     		}else{
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
		}

		@Override
		protected void onResume() {
			super.onResume();
		}
	    
	    
	    
	}

