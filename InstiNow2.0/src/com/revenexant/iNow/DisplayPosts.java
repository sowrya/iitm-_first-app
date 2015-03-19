
package com.revenexant.iNow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.revenexant.iNow.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayPosts extends Fragment {
	
	SharedPreferences save;
	private JsonParser jp = new JsonParser();
	private JSONObject jObj;
	private static final String url="https://students.iitm.ac.in/mobops_testing/displayposts.php";
	static Random r = new Random();
	private static LinearLayout displin;
	private static Button DispLoad;
	private static TextView ping;
	private static ProgressBar dispspin;
	private static int success = 0;
	private static String output;
	private static String[] heading;
	private static String[] box;
	private static Activity ring;
	
	public static DisplayPosts newInstance() {
		DisplayPosts fragment = new DisplayPosts();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	
	public DisplayPosts(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ring = getActivity();
		save = ring.getSharedPreferences(getString(R.string.sharedprefkey),Context.MODE_PRIVATE);
		View rootView = inflater.inflate(R.layout.frag_displayposts, container, false);
		displin = (LinearLayout) rootView.findViewById(R.id.lindisplay);

		if(save.contains("displayposts")){
			try {
				JSONObject jObj = new JSONObject(save.getString("displayposts", null));
				initializeAll(jObj);
				displin.removeAllViews();
				changeStuffUp();
			} catch (JSONException e) {
				Log.d("SavedDisplay", e.toString());
			}
		}
		DispLoad = (Button) rootView.findViewById(R.id.dispload);
		DispLoad.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!jp.checkInternetConnection(ring)){
					Toast.makeText(ring, "No net connection.", Toast.LENGTH_SHORT).show();
				} else {
					loading();
					new GetDisplayPosts().execute();
				}
				
			}
		});
		
		return rootView;
	}
	
	private void loading(){
		displin.removeAllViews();
		dispspin = new ProgressBar(getActivity());
		dispspin.setVisibility(View.VISIBLE);
		//displin.removeView(DispLoad);
		displin.addView(dispspin);
	}
	public void changeStuffUp() {
		displin.removeView(dispspin);
		if(success==0){
			ping = new TextView(ring);
			ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			ping.setText("No posts to display at the moment."); ping.setVisibility(View.VISIBLE);
			ping.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			ping.setTextSize(20);
			displin.addView(ping);
		} else {
			try{
				ListView displist = new ListView(ring);
				displist.setBackgroundColor(Color.TRANSPARENT);
				displist.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				ArrayAdapter<String> aa = new ArrayAdapter<String>(ring, android.R.layout.simple_list_item_1, heading);
				displist.setAdapter(aa);
				displist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						try {
							((UserChoices) ring).popUpStarter(heading[position], box[position]);
						} catch (Exception e) {
							Log.d("Started", e.toString());
						}
					}
				});
				displin.addView(displist);} catch(Exception ei){Log.v("Display", ei.toString());}
			//} //end for
		} //end else
	}
	
	class GetDisplayPosts extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
     		//making sure the input is in Capital letters
     		users.add(new BasicNameValuePair("roll", "check"));
     		output = jp.makeHttpRequest(url, "GET", users);
			try{jObj = jp.returnJson();}catch(Exception e){Log.v("JSON", "Line 1.");}
			try{
				success = jObj.getInt("success");
				
				heading = new String[success];  box = new String[success];
					for(int i=0;i<success && i<100;i++){
						try{
						//heading[i] = jObj.getString("id"+i)+"\t"+jObj.getString("user"+i)+": "+jObj.getString("title"+i);
							heading[i] = jObj.getString("title"+i);
						box[i] = jObj.getString("content"+i)+"\n"+jObj.getString("created_at"+i)+"  "
						+jObj.getInt("solved"+i)+"  "+jObj.getString("solved_at"+i)+"\t"+
								jObj.getString("avg_anger"+i);
						} catch(Exception be){Log.e("Multiple", be.toString());}
					}
				} catch(Exception e){
					Log.e("JSON", e.toString());
				}
			return null;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			DisplayPosts dp = new DisplayPosts();
			dp.changeStuffUp();
			dp.save(output);}
		
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			super.onAttach(activity);
			((UserChoices) activity).onSectionAttached(5);
		} catch (Exception e) {
			Log.v("Attach",e.toString());
		}
	}

	public static void initializeAll(JSONObject jObj) {

		try {
			success = jObj.getInt("success");
		
		heading = new String[success];  box = new String[success];
			for(int i=0;i<success && i<100;i++){
				try{
				heading[i] = jObj.getString("id"+i)+"\t"+jObj.getString("user"+i)+": "+jObj.getString("title"+i);
				box[i] = jObj.getString("content"+i)+"\n"+jObj.getString("created_at"+i)+"  "
				+jObj.getInt("solved"+i)+"  "+jObj.getString("solved_at"+i)+"\t"+
						jObj.getString("avg_anger"+i);
				} catch(Exception be){Log.e("Multiple", be.toString());}
			}
		} catch (JSONException e) {
			Log.e("ExternalJsonParsing", e.toString());
			}
		
	}

	public void save(String makeHttpRequest) {
		try {
			
			SharedPreferences.Editor editor = ring.getSharedPreferences(getString(R.string.sharedprefkey),Context.MODE_PRIVATE).edit();
			editor.putString("displayposts", makeHttpRequest);

		} catch (Exception e) {
			Log.e("SharedPref","saving error "+e.toString());
		}
		
	}
	
	
	

}
