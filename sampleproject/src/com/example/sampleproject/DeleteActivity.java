package com.example.sampleproject;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DeleteActivity extends ListActivity {
	private ArrayList<String> results = new ArrayList<String>();
	DatabaseManager db;
	Rule rule;
	ArrayList<Rule> allRules=new ArrayList<Rule>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);

		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		progressBar.setIndeterminate(true);
		getListView().setEmptyView(progressBar);

		// Must add the progress bar to the root of the layout
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(progressBar);
		openAndQueryDatabase();

		displayResultList();
	}

	private void displayResultList() {
		TextView tView = new TextView(this);
		tView.setText("Rules in the database");
		getListView().addHeaderView(tView);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, results));
		getListView().setTextFilterEnabled(true);
		getListView().setClickable(true);

		/*getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});*/

		getListView().setOnItemClickListener( new OnItemClickListener() { 
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				Intent i = new Intent(UpdateActivity.this,DoUpdateActivity.class);
				startActivity(i);*/
				rule= allRules.get(position-1);
				
			
				db = new DatabaseManager(DeleteActivity.this);
				AlertDialog.Builder builder = new AlertDialog.Builder(DeleteActivity.this);
				builder.setCancelable(true);
				builder.setTitle("Confirm");
				builder.setMessage("Do you want to delete this rule?");
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Boolean status= db.deleteRule(rule);
						if(status == false){
							AlertDialog.Builder alert = new AlertDialog.Builder(DeleteActivity.this);
							alert.setMessage("Rule not deleted!");
							alert.setCancelable(true);
							AlertDialog newdialog = alert.create();
							newdialog.show();
						}
						
					}
				});
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});   
	}

	private void openAndQueryDatabase(){
		try{
			DatabaseManager db = new DatabaseManager(this);
			allRules = db.getAllRows();
			Iterator<Rule> itr= allRules.iterator();

			while(itr.hasNext()){
				Rule temp = itr.next();
				String s= temp.getIpAddress()+"\t"+ temp.getWebsiteAddress()+"\t"+ temp.getAction();
				results.add(s);
			}
		}catch(SQLiteException se){
			Log.e(getClass().getSimpleName(), "Could not create or Open the database");
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_delete, menu);
		return true;
	}

}
