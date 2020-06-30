package com.collegeproject.feelsecure;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				/*
				 *  After the splash image has been shown now start the activity through intent 
				 *  which shows what to do and how to configure the application
				 *  start the activity-> BasicInfoActivity
				 */
				SharedPreferences userprefence=getApplicationContext().getSharedPreferences("FeelSecurePref",MODE_PRIVATE);
				
				if(userprefence.getBoolean("FirstTime",true)){
					Intent startbasicinfoactivity= new Intent(getApplicationContext(),BasicInfoActivity.class);
					startActivity(startbasicinfoactivity);
					finish();
				}
				else{
					Intent startbasicinfoactivity= new Intent(getApplicationContext(),ConfigurationActivity.class);
					startActivity(startbasicinfoactivity);
					finish();
				}
						
				
			}},2500);
	}
}
