package com.example.sampleproject;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add, menu);
		return true;
	}

	public void addRule(View view){
		try{
			//Since inserting a row and other processes are async calls, creating an AsyncTask to get this job done
			new Insert().execute();
		}
		catch(Exception e){
			//Catch the exception and thrown on the main screen
			TextView text = (TextView) findViewById(R.id.textView);
			text.setText("Exception: " + e.getMessage());
		}
	}

	private class Insert extends AsyncTask<URL,Integer,Long>{
		int error =Constants.NO_ERROR;
		Resources res=getResources();
		int flag=0;
		String msg="";

		protected Long doInBackground(URL... urls){
			try{
				EditText ipET= (EditText) findViewById(R.id.ipaddress);
				String ipaddress=ipET.getText().toString();
				EditText wET= (EditText) findViewById(R.id.website);
				String website=wET.getText().toString();
				EditText aET=(EditText)findViewById(R.id.action);
				String action=aET.getText().toString();
				Rule rule=new Rule();
				DatabaseManager dbm= new DatabaseManager(AddActivity.this);


				if(!invalidIP(ipaddress)){
					String Error = res.getString(R.string.INVALID_IPADDRESS_WEBSITE);
					throw new IllegalArgumentException(Error);
				}

				if(!invalidSite(website)){
					String Error = res.getString(R.string.INVALID_IPADDRESS_WEBSITE);
					throw new IllegalArgumentException(Error);
				}

				rule.setIpAddress(ipaddress);
				rule.setWebsiteAddress(website);
				rule.setAction(action);

				Boolean status = dbm.addRule(rule);
				
				if(status == false){
					AlertDialog.Builder alert = new AlertDialog.Builder(AddActivity.this);
					alert.setMessage("Rule not added!");
					alert.setCancelable(true);
					AlertDialog dialog = alert.create();
					dialog.show();
				}
				if(status == true){
					msg="Rule has been added";
					flag=1;
				}
				
			}
			catch(IllegalArgumentException e){
				error = Constants.ILLEGAL_ARGUMENT_EXCEPTION;
			}
			return (long)1;
		}

		protected void onPostExecute(Long result) {
			if(error==Constants.ILLEGAL_ARGUMENT_EXCEPTION){
				AlertDialog.Builder alert = new AlertDialog.Builder(AddActivity.this);
				alert.setTitle(Constants.ERROR_DIALOG);
				String Error = res.getString(R.string.INVALID_IPADDRESS_WEBSITE);
				alert.setMessage(Error);
				alert.setCancelable(true);
				AlertDialog dialog = alert.create();
				dialog.show();
			}
			if(flag==1){
				TextView text = (TextView) findViewById(R.id.textView);
				text.setText(msg);
			}
		}

		private boolean invalidIP(String ipAddr){
			Pattern p = Pattern.compile(Constants.IP_REGEX);
			Matcher m = p.matcher(ipAddr);
			if(m.find()){
				return true;
			}
			else
				return false;
		}

		private boolean invalidSite(String website){
			Pattern p = Pattern.compile(Constants.WEBSITE_REGEX);
			Matcher m = p.matcher(website);
			if(m.find()){
				return true;
			}
			else
				return false;
		}


	}
}
