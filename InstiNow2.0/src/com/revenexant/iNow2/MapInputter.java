package com.revenexant.iNow2;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapInputter extends Fragment{
	
	public static MapInputter newInstance() {
		MapInputter fragment = new MapInputter();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}
	
	public MapInputter() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_mapinput, container, false);
		return rootView;}
	
	
	public void onAttach(Activity activity) {
		try {
			super.onAttach(activity);
			((UserChoices) activity).onSectionAttached(5);
		} catch (Exception e) {
			Log.v("Attach",e.toString());
		}
	}
}
