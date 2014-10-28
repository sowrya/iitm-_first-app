package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
			 			LoginAttempt();
		    	} else {
		    		     Toast.makeText(MainActivity.this,"No internet connection.",Toast.LENGTH_LONG).show();
		    		    }
		 return czech;
		   }//end of NetCheck
	 
	 private void LoginAttempt(){
		 int success=0;
     	try {
     		List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
     		users.add(new BasicNameValuePair("username", username1.getText().toString()));
     		users.add(new BasicNameValuePair("password", password1.getText().toString()));
     		try{
     			JSONObject json = jsonparser.makeHttpRequest(url, "POST", users);
         		success = json.getInt("success"); //failing here please fix
     		} catch(Exception e){
     			Toast.makeText(MainActivity.this,"JSON fail.",Toast.LENGTH_LONG).show();
     		}
     		
     		if (success == 1) {
     			logintest=true;
     			Toast.makeText(MainActivity.this,"Welcome!",Toast.LENGTH_LONG).show();
     		}else{logintest=false;
     			Toast.makeText(MainActivity.this,"Invalid credentials.",Toast.LENGTH_LONG).show();   
     		}
     	} catch (Exception e) {
     		Toast.makeText(MainActivity.this, "LoginAttempt fail.", Toast.LENGTH_LONG).show();
     	}
	 }// end of LoginAttempt

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

