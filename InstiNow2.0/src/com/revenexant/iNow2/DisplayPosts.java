package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayPosts extends Fragment {
	
	private JsonParser jp = new JsonParser();
	private JSONObject jObj;
	private static final String url="http://students.iitm.ac.in/mobops_testing/displayposts.php";
	static Random r = new Random();
	private static LinearLayout lin;
	private static TextView ping;
	private static int success = 0;
	private static String[] heading;
	private static String[] box;
	private static Activity ring;
	private static Resources ringRes;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public DisplayPosts(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_displayposts, container, false);
		lin = (LinearLayout) rootView.findViewById(R.id.lindisplay);
		ring = getActivity(); ringRes = getResources();
		if(!jp.checkInternetConnection(ring)){
			Toast.makeText(ring, "No net connection.", Toast.LENGTH_SHORT).show();
		} else {
			new GetPosts().execute();
		}
		return rootView;
	}
	
	class GetPosts extends AsyncTask<String, String, String>{
		

		@Override
		protected String doInBackground(String... params) {
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
     		//making sure the input is in Capital letters
     		users.add(new BasicNameValuePair("roll", "check"));
			jp.makeHttpRequest(url, "POST", users);
			try{jObj = jp.returnJson();}catch(Exception e){Log.v("JSON", "Line 1.");}
			try{
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
				} catch(Exception e){
					Log.e("JSON", e.toString());
				}
			return null;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			DisplayPosts.changeStuffUp();}
		
	}

	public static void changeStuffUp() {
		if(success==0){
			try{
			ping = new TextView(ring);
			ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			ping.setText("No posts to display at the moment."); ping.setVisibility(View.VISIBLE);
			ping.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			ping.setTextSize(20);
			lin.addView(ping);} catch(Exception ei){Log.v("Display", ei.toString());}
		} else {
			for(int i=0;i<success && i<100;i++){
				try{
				ping = new TextView(ring);
				ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				ping.setText(heading[i]); ping.setVisibility(View.VISIBLE);
				ping.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
				ping.setTextSize(20); ping.setId(0x00+i);
				lin.addView(ping);
				ping = new TextView(ring);
				ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				ping.setText(box[i]); ping.setVisibility(View.VISIBLE);
				if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				    ping.setBackgroundDrawable(ringRes.getDrawable(R.drawable.borderradius) );
				} else {
					ping.setBackground(ringRes.getDrawable(R.drawable.borderradius));
				}
				ping.setTextSize(18); ping.setId(0xd00+i);
				lin.addView(ping);} catch(Exception be){Log.e("Multiple", be.toString());}
			}
		}
		
	}

}
