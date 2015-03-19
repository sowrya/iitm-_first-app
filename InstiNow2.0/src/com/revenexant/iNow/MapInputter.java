package com.revenexant.iNow;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapInputter extends Fragment{
	
	private static GoogleMap mMap;
	private static Double latitude, longitude;
	
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
		View rootView = null;
		try {
			rootView = inflater.inflate(R.layout.frag_t5e, container, false);
			 latitude = 12.986608;
	         longitude = 80.234265;
	      // Do a null check to confirm that we have not already instantiated the map.
	 	    /**if (mMap == null) {
	 	        // Try to obtain the map from the MapFragment.
	 	        mMap = ((MapFragment) UserChoices.fragmentManager
	 	                .findFragmentById(R.id.mapinputter)).getMap();
	 	        // Check if we were successful in obtaining the map.
	 	        if (mMap != null)
	 	            {setUpMap();}
	 	    }*/
	         
		} catch (Exception e) {
			Log.e("onCreateView", e.toString());
		}
		return rootView;}
	
	
	@Override
	public void onAttach(Activity activity) {
		try {
			super.onAttach(activity);
			((UserChoices) activity).onSectionAttached(6);
		} catch (Exception e) {
			Log.v("Attach",e.toString());
		}
	}
/**	
	private static void setUpMap() {
	    // For showing a move to my location button
	    mMap.setMyLocationEnabled(true);
	    // For dropping a marker at a point on the Map
	    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
	    // For zooming automatically to the Dropped PIN Location
	    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
	            longitude), 12.0f));
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    if (mMap != null)
	        setUpMap();

	    if (mMap == null) {
	        // Try to obtain the map from the SupportMapFragment.
	        mMap = ((MapFragment) UserChoices.fragmentManager
	                .findFragmentById(R.id.mapinputter)).getMap(); // getMap is deprecated
	        // Check if we were successful in obtaining the map.
	        if (mMap != null)
	            setUpMap();
	    }
	}

	// The mapfragment's id must be removed from the FragmentManager
	// or else if the same it is passed on the next time then 
	// application will crash
	@Override
	public void onDestroyView() {
	    super.onDestroyView();
	    if (mMap != null) {
	        UserChoices.fragmentManager.beginTransaction()
	            .remove(UserChoices.fragmentManager.findFragmentById(R.id.mapinputter)).commit();
	        mMap = null;
	    }
	}**/
	}