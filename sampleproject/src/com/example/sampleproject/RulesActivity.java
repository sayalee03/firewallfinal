package com.example.sampleproject;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class RulesActivity extends Activity {
	
	public void addClick(View view){
		Intent intent = new Intent(this, AddActivity.class);
		/*EditText editText = (EditText) findViewById(R.id.edit_message);
	    String message = editText.getText().toString();
	    intent.putExtra(EXTRA_MESSAGE, message);*/
	    startActivity(intent);
	}
	
	public void updateClick(View view){
		Intent intent = new Intent(this, UpdateActivity.class);
		/*EditText editText = (EditText) findViewById(R.id.edit_message);
	    String message = editText.getText().toString();
	    intent.putExtra(EXTRA_MESSAGE, message);*/
	    startActivity(intent);
	}

	public void deleteClick(View view){
		Intent intent = new Intent(this, DeleteActivity.class);
		/*EditText editText = (EditText) findViewById(R.id.edit_message);
	    String message = editText.getText().toString();
	    intent.putExtra(EXTRA_MESSAGE, message);*/
	    startActivity(intent);
	}
	
	public void viewClick(View view){
		Intent intent = new Intent(this, ViewActivity.class);
		/*EditText editText = (EditText) findViewById(R.id.edit_message);
	    String message = editText.getText().toString();
	    intent.putExtra(EXTRA_MESSAGE, message);*/
	    startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rules);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_rules, menu);
		return true;
	}

}
