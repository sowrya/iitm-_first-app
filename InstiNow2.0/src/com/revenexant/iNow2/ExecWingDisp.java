package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ExecWingDisp extends Fragment {
	
	private JsonParser jp = new JsonParser();
	private JSONObject jObj;
	private static final String url="http://students.iitm.ac.in/mobops_testing/execwingposts.php";
	static Random r = new Random();
	private static LinearLayout execlin;
	private static Button ExecLoad;
	private static TextView ping;
	private static ProgressBar execspin;
	private static int success = 0;
	private static String[] heading;
	private static String[] box;
	private static Activity ring;
	private static Resources ringRes;
	
	
	public ExecWingDisp() {
		
	}
	public static ExecWingDisp newInstance(int sectionNumber) {
    	ExecWingDisp fragment = new ExecWingDisp();
		Bundle args = new Bundle();
		args.putInt("section_number", sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_execwing, container, false);
		execlin = (LinearLayout) rootView.findViewById(R.id.execlindisplay);
		ExecLoad = (Button) rootView.findViewById(R.id.execload);
		ExecLoad.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!jp.checkInternetConnection(ring)){
					Toast.makeText(ring, "No net connection.", Toast.LENGTH_SHORT).show();
				} else {
					loading();
					new GetExecPosts().execute();
				}
				
			}
		});
		ring = getActivity(); ringRes = getResources();
		return rootView;
	}
	
	private void loading() {
		execspin = new ProgressBar(getActivity());
		execspin.setVisibility(View.VISIBLE);
		execlin.removeView(ExecLoad);
		execlin.addView(execspin);
		
	}
	
	class GetExecPosts extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			List<BasicNameValuePair> users = new ArrayList<BasicNameValuePair>();
     		//null input. garbage
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
			ExecWingDisp.changeStuffUp();}
		
	}

	public static void changeStuffUp() {
		execlin.removeView(execspin);
		if(success==0){
			try{
			ping = new TextView(ring);
			ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			ping.setText("No posts to display at the moment."); ping.setVisibility(View.VISIBLE);
			ping.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			ping.setTextSize(20);
			execlin.addView(ping);} catch(Exception ei){Log.v("Display", ei.toString());}
		} else {
			for(int i=0;i<success && i<100;i++){
				try{
				ping = new TextView(ring);
				ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				ping.setText(heading[i]); ping.setVisibility(View.VISIBLE);
				ping.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
				ping.setTextSize(20); ping.setId(0x00+i);
				execlin.addView(ping);
				ping = new TextView(ring);
				ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				ping.setText(box[i]); ping.setVisibility(View.VISIBLE);
				ping.setBackground(ringRes.getDrawable(R.drawable.borderradius));
				ping.setTextSize(18); ping.setId(0xd00+i);
				execlin.addView(ping);} catch(Exception be){Log.e("Multiple", be.toString());}
			}
		}
		
	}
	@Override
	public void onAttach(Activity activity) {
	super.onAttach(activity);
	((UserChoices) activity).onSectionAttached(getArguments().getInt(
			"section_number"));
	}
}
