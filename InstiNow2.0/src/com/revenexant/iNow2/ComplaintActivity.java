package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ComplaintActivity extends Fragment {

	private static EditText title,content;
    private static Button submit;
    private static List<CheckBox> tags = new ArrayList<CheckBox>();
    JsonParser jsonparser=new JsonParser();
    private static final String url="http://students.iitm.ac.in/mobops_testing/complaint.php";
	private static final String ARG_SECTION_NUMBER = "nothing";
    public static int count;
    
    
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
		} catch(Exception e){
			Log.e("getViewCup", e.toString());}
		try{
        
        submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{new CreateComplaint().execute();}
	 			catch(Exception e){
	 				Log.e("ASync:",e.toString());}//check for AsyncTask error
			}
		});} catch(Exception e){
		Log.e("onClickCup", e.toString());
		}
		return rootView;
	}
    
    
class CreateComplaint extends AsyncTask<String, String, String>{
	

	@Override
	protected String doInBackground(String... params) {
		int success = 0;
        String title1 = title.getText().toString();
        String content1 = content.getText().toString();
        try {
            // Building Parameters
        	List<BasicNameValuePair> posts = new ArrayList<BasicNameValuePair>();
            posts.add(new BasicNameValuePair("title2", title1));
            posts.add(new BasicNameValuePair("content2", content1));
            for(int i=0;i<9;i++){
            	if(tags.get(i).isChecked()){
            		posts.add(new BasicNameValuePair("tag"+i,tags.get(i).getText().toString()));
            		count=count+1;
            	}
            	else{
            		continue;
            	}
            }
            posts.add(new BasicNameValuePair("count",Integer.toString(count)));
            //Posting user data to script 
             
            try{String s =jsonparser.makeHttpRequest(url, "POST", posts);
            	success = Integer.parseInt(s.substring(s.length()-4, s.length()-3));
            	//change the php script
            } catch(Exception e){
            	Log.e("Json", e.toString());
            }
            if (success == 1) {
                Log.d("Complaint registered!", "Successful.");                  
                //finish();
                //what is this finish???
                return "success";
            }else{
                Log.d("Failure!", "Unsuccessful");
                return "message";
                
            }
        } catch (Exception e) {
            Log.e("doInBack", e.toString());
        }

		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
	
}

}
