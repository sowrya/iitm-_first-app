package com.revenexant.iNow2;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

public class UserChoices extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private boolean finishthis = false;
	SharedPreferences save;
	private CharSequence[] mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usernavdrawer);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getResources().getStringArray(R.array.navdrawer_items);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}
	
	

	@Override
	public void onBackPressed() {
		 new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
         .setMessage("Are you sure?")
         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {

                 Intent intent = new Intent(Intent.ACTION_MAIN);
                 intent.addCategory(Intent.CATEGORY_HOME);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(intent);
                 finish();
             }
         }).setNegativeButton("No", null).show();
	}






	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		
		switch(position){
		case 3:
			fragmentManager.beginTransaction().replace(R.id.container,
					ComplaintActivity.newInstance(position + 1)).commit();
			break;
		case 4:
			try{fragmentManager.beginTransaction().replace(R.id.container,new DisplayPosts()).commit();}
			catch(Exception e){Log.e("DisplayPost", "can't even begin");}
			break;
		default:
			fragmentManager.beginTransaction().replace(R.id.container,
					PlaceholderFragment.newInstance(position + 1)).commit();
			break;
		}
	}

	public void onSectionAttached(int number) {
		getActionBar().setTitle(mTitle[number-1]);
		}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.logout:
			return runLogout();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	//the runLogout function if logout is pressed.
	
	public boolean runLogout() {
		finishthis = true;
		//enter logging out code here please
		save = getSharedPreferences(getString(R.string.sharedprefkey), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = save.edit();
		editor.remove("loggedin");
		editor.remove("username");
		editor.commit();
		Intent i = new Intent(UserChoices.this, MainActivity.class);
        startActivity(i);
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private static int frags[] = {R.layout.frag_execwing,R.layout.frag_instievents,R.layout.frag_t5e,R.layout.frag_execwing,};

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(frags[getArguments().getInt(ARG_SECTION_NUMBER)-1], 
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((UserChoices) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
