package com.revenexant.iNow2;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ComplaintActivity extends Activity {

	private static EditText title,content;
    private static Button submit;
    private static List<CheckBox> tags = new ArrayList<CheckBox>();
    JsonParser jsonparser=new JsonParser();
    private static final String url="http://students.iitm.ac.in/mobops_testing/complaint.php";
    public static int count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complaint);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		title=(EditText) findViewById(R.id.editText1);
		content=(EditText) findViewById(R.id.editText2);
        submit=(Button) findViewById(R.id.button1);
        tags.add(0,(CheckBox) findViewById(R.id.checkBox1));
        tags.add(1,(CheckBox) findViewById(R.id.checkBox2));
        tags.add(2,(CheckBox) findViewById(R.id.checkBox3));
        tags.add(3,(CheckBox) findViewById(R.id.checkBox4));
        tags.add(4,(CheckBox) findViewById(R.id.checkBox5));
        tags.add(5,(CheckBox) findViewById(R.id.checkBox6));
        tags.add(6,(CheckBox) findViewById(R.id.checkBox7));
        tags.add(7,(CheckBox) findViewById(R.id.checkBox8));
        tags.add(8,(CheckBox) findViewById(R.id.checkBox9));
        
        
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new CreateComplaint().execute();
			}
		});

	}
class CreateComplaint extends AsyncTask<String, String, String>{
	private ProgressDialog pdialog;
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
        pdialog = new ProgressDialog(ComplaintActivity.this);
        pdialog.setMessage("Registering the complaint");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(true);
        pdialog.show();
	};

	@Override
	protected String doInBackground(String... params) {
		int success;
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
            success = jsonparser.makeHttpRequest(url, "POST", posts);
            if (success == 1) {
                Log.d("Complaint registered!", "Successful.");                  
                finish();
                return "success";
            }else{
                Log.d("Failure!", "Unsuccessful");
                return "message";
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		pdialog.dismiss();
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
	}
	
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complaint, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
