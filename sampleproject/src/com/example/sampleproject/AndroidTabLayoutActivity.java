package com.example.sampleproject;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AndroidTabLayoutActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_tab_layout);
		
		TabHost tabHost = getTabHost();
		
	    // Tab for Rules
	    TabSpec firewallspec = tabHost.newTabSpec("Start Firewall");
	    // setting Title and Icon for the Tab
	    firewallspec.setIndicator("Start Firewall");
	    Intent firewallIntent = new Intent(this, StartFirewallActivity.class);
	    firewallspec.setContent(firewallIntent);
		 
	    // Tab for Rules
	    TabSpec rulespec = tabHost.newTabSpec("Rules");
	    // setting Title and Icon for the Tab
	    rulespec.setIndicator("Rules");
	    Intent rulesIntent = new Intent(this, RulesActivity.class);
	    rulespec.setContent(rulesIntent);
	    
	    /*TabSpec logspec = tabHost.newTabSpec("Log");
	    // setting Title and Icon for the Tab
	   logspec.setIndicator("Log");
	    Intent logIntent = new Intent(this, LogActivity.class);
	    logspec.setContent(logIntent);*/

	    tabHost.addTab(firewallspec);	// Adding firewall tab
	    tabHost.addTab(rulespec); // Adding rules tab
	    //tabHost.addTab(logspec);  // Adding log tab
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_android_tab_layout, menu);
		return true;
	}

}
