package com.revenexant.iNow2;

import java.util.Random;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopUpActivity extends Fragment{
	
	private TextView ping;
	
	public static PopUpActivity newInstance(String heading,String box) {
    	PopUpActivity fragment = new PopUpActivity();
		Bundle args = new Bundle();
		args.putString("heading", heading);
		args.putString("box", box);
		fragment.setArguments(args);
		return fragment;
	}
	
	public PopUpActivity(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.popup_page, container, false);
		Random r = new Random();
		LinearLayout displin = (LinearLayout) rootView.findViewById(R.id.popuplin);
		try {
			ping = new TextView(getActivity());
			ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			ping.setText(getArguments().getString("heading","ERROR")); ping.setVisibility(View.VISIBLE);
			ping.setBackgroundColor(Color.WHITE);
			ping.setTextSize(20);
			displin.addView(ping);
			ping = new TextView(getActivity());
			ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			String fini = repairerPs(getArguments().getString("box","Nothing to display."));
			ping.setText(fini); ping.setVisibility(View.VISIBLE);
			ping.setBackground(getResources().getDrawable(R.drawable.borderradius));
			ping.setMovementMethod(new ScrollingMovementMethod());
			ping.setPadding(10, 10, 10, 10);
			ping.setTextSize(18);
			displin.addView(ping);
			} catch (Exception e) {
			Log.e("PopUpCreateView", e.toString());
		}
		return rootView;
	}

	private String repairerPs(String string) {
		String out = "";boolean pass = false;
		for (int i=0;i<string.length();i++){
			if(string.charAt(i)=='<'){
				pass = true;
				out+="\n";
			}
			if(!pass){
				out+=string.charAt(i);
			}
			if(string.charAt(i)=='>'){
				pass=false;
			}
		}
		return out;
	}
	

}
