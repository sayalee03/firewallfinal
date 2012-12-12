package com.example.sampleproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class LogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		try {
		      Process process = Runtime.getRuntime().exec("logcat -I");
		      BufferedReader bufferedReader = new BufferedReader(
		      new InputStreamReader(process.getInputStream()));
		                       
		      StringBuilder log=new StringBuilder();
		      String line;
		      while ((line = bufferedReader.readLine()) != null) {
		        log.append(line);
		      }
		      TextView tv = (TextView)findViewById(R.id.textView1);
		      tv.setText(log.toString());
		    } catch (IOException e) {
		    }
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_log, menu);
		return true;
	}

}
