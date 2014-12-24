package com.revenexant.iNow2;

import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopUpActivity extends Activity{
	
	private TextView ping;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_page);
		Random r = new Random();
		LinearLayout displin = (LinearLayout) findViewById(R.id.popuplin);
		ping = new TextView(this);
		ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		ping.setText(savedInstanceState.getString("heading","ERROR")); ping.setVisibility(View.VISIBLE);
		ping.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
		ping.setTextSize(20);
		displin.addView(ping);
		ping = new TextView(this);
		ping.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		ping.setText(savedInstanceState.getString("box","Nothing to display.")); ping.setVisibility(View.VISIBLE);
		ping.setBackground(getResources().getDrawable(R.drawable.borderradius));
		ping.setTextSize(18);
		displin.addView(ping);
		
	}
	
	

}
