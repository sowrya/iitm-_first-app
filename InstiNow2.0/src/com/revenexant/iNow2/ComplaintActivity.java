package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ComplaintActivity extends Fragment {

	private static EditText title,content;
	private static TextView pain;
	private static RatingBar avg_anger;
	private static String user_id;
    private static Button submit;
    SharedPreferences save;
    private static List<CheckBox> tags = new ArrayList<CheckBox>();
    JsonParser jsonparser=new JsonParser();
    private static final String url="http://students.iitm.ac.in/mobops_testing/complaint.php";
	private static final String ARG_SECTION_NUMBER = "nothing";
	private static JSONObject jObj = null;
    
    
    public ComplaintActivity(){
    	
    }
	public static ComplaintActivity newInstance(int sectionNumber) {
    	ComplaintActivity fragment = new ComplaintActivity();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_feedback,container, false);
		try{
		title=(EditText) rootView.findViewById(R.id.editText1);
		content=(EditText) rootView.findViewById(R.id.editText2);
		pain = (TextView) rootView.findViewById(R.id.pain);
		avg_anger = (RatingBar) rootView.findViewById(R.id.ratingBar1);
        submit=(Button) rootView.findViewById(R.id.button1);
        tags.add(0,(CheckBox) rootView.findViewById(R.id.checkBox1));
        tags.add(1,(CheckBox) rootView.findViewById(R.id.checkBox2));
        tags.add(2,(CheckBox) rootView.findViewById(R.id.checkBox3));
        tags.add(3,(CheckBox) rootView.findViewById(R.id.checkBox4));
        tags.add(4,(CheckBox) rootView.findViewById(R.id.checkBox5));
        tags.add(5,(CheckBox) rootView.findViewById(R.id.checkBox6));
        tags.add(6,(CheckBox) rootView.findViewById(R.id.checkBox7));
        tags.add(7,(CheckBox) rootView.findViewById(R.id.checkBox8));
        tags.add(8,(CheckBox) rootView.findViewById(R.id.checkBox9));
        save = getActivity().getSharedPreferences(getString(R.string.sharedprefkey), Context.MODE_PRIVATE);
        user_id = save.getString("username", "guest");//guest if user name not provided.
		} catch(Exception e){
			Log.e("getViewCup", e.toString());}
        
        submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(title.getText().toString().isEmpty()){
					Toast.makeText(getActivity(), "Title cannot be empty.", Toast.LENGTH_SHORT).show();
				}else if(content.getText().toString().isEmpty()){
					Toast.makeText(getActivity(), "Content cannot be empty.", Toast.LENGTH_SHORT).show();
				}else {int count=0;
					for(int i=0;i<9;i++){
						if(tags.get(i).isChecked()){count++;}}
					if(count==0){
					Toast.makeText(getActivity(), "Select at least one criteria.", Toast.LENGTH_SHORT).show();
					}else if(count>4){
						Toast.makeText(getActivity(), "Too many criteria selected. Max 4.", Toast.LENGTH_SHORT).show();
					}else{
						try{new CreateComplaint().execute();}
						catch(Exception e){
							Log.e("ASync:",e.toString());}//check for AsyncTask error
					}
				}//count else
			}// onClick method end
		});//end of onClick
        avg_anger.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				int i = (int)rating;
				switch(i){
				case 1:
					pain.setText("Messy hair.");break;
				case 2:
					pain.setText("Bad Cold.");break;
				case 3:
					pain.setText("Speak to prof about attendance.");break;
				case 4:
					pain.setText("No internet connection!");break;
				case 5:
					pain.setText("Just had a break-up.");break;
				default:
					pain.setText("Do you really have an issue?");break;
				}
				
			}
		});
        
		return rootView;
	}// end of onCreateView
    
    
class CreateComplaint extends AsyncTask<String, String, String>{
	

	@Override
	protected String doInBackground(String... params) {
		int success = 0;
		int count = 0;
        String title1 = title.getText().toString();
        String content1 = content.getText().toString();
        try {
        	Looper.prepare();
            // Building Parameters
        	List<BasicNameValuePair> posts = new ArrayList<BasicNameValuePair>();
        	posts.add(new BasicNameValuePair("user_id", user_id));
        	Log.v("username", user_id);
            posts.add(new BasicNameValuePair("title2", title1));
            posts.add(new BasicNameValuePair("content2", content1));
            posts.add(new BasicNameValuePair("avg_anger", avg_anger.getRating()+""));
            Time t = new Time(); t.setToNow();
            posts.add(new BasicNameValuePair("created_at", t.toString()));
            //time format is YYYYMMDDTHHMMSS so change php accordingly
            Log.v("time", t.toString());
            for(int i=0;i<9;i++){
            	if(tags.get(i).isChecked()){
            		posts.add(new BasicNameValuePair("tag"+count,tags.get(i).getText().toString()));
            		count++;
            	}
            	else{
            		continue;
            	}
            }
            posts.add(new BasicNameValuePair("count",Integer.toString(count)));
            //Posting user data to script 
             
            try{String s =jsonparser.makeHttpRequest(url, "POST", posts);
            jObj = new JSONObject(s);
            success = jObj.getInt("success");
            Log.v("message", jObj.getString("message"));
            } catch(Exception e){
            	Log.e("Json", e.toString());
            }
            if (success == 1) {
                Log.d("Complaint registered!", "Successful.");
            }else{
                Log.d("Failure!", "Unsuccessful");
                Looper.loop();
            }
        } catch (Exception e) {
            Log.e("doInBack", e.toString());
        }
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Intent i = new Intent(getActivity(), Thanks.class);
        startActivity(i);}
	
}

}
