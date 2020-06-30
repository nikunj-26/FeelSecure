package com.collegeproject.feelsecure;

import android.os.Bundle;
import android.view.Window;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class BasicInfoActivity extends Activity {

	/*
	 * public static final string information message give detail about the s/w
	 * and how to use the software and how configure it and use it 
	 */
	
public static final String Informationmsg = "Hello, User Welcome to our application,FeelSecure  " +"\n"+
		"1) Enter or add the maximum 3 person whom you want to contact when you are in Danger.\n\n" +
		"2)For this application to work properly you need a properly Working Internet connction.\n\n" +
		"And minimum Balance Required to send message to the number of person you have added during configuration.\n\n" +
		"3)please click on 'OK' to Continue and start the configuration of the application. Thank You :)";

public static final String configmsg="1)Long Hold the TextBox to Select From the phone book \n 2)Select all the 3 phone numbers.";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic_info);
		
		
		
		/* Information dialog show the information about how to use the s/w
		 * it will start as soon as the main activity starts
		 * and when the dialog box is dismissed it will start new Activity
		 */
		AlertDialog.Builder informationdialog= new AlertDialog.Builder(BasicInfoActivity.this);
		
		//second dialog box object creation
		final AlertDialog.Builder configalert=new AlertDialog.Builder(this);
		// setting the title of the dialog box
		informationdialog.setTitle("How to use the Software");
		
		// setting the message to display 
		informationdialog.setMessage(Informationmsg);
		
		//setting alert dialog with one button.
		 informationdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog,int which) {

				/* when user read about the working of s/w 
				 * show the second dialog box about how to Enter the phone number and save it 
				 *  
				 */
				configalert.show();
			}
		});
		
		 informationdialog.show();
		 
		 //Second Dialog box
		 
		 			
			//final alert object to dismiss the dialog box when user clicks on the okay button
			final AlertDialog candialog = configalert.create();
			// setting the title of alert dialog box
			configalert.setTitle("Information");
			
			//setting the message of alert dialog box
			configalert.setMessage(configmsg);
			
			configalert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				
				
				public void onClick(DialogInterface dialog, int which) {
					/*When user Reads the required information and clicks on Okay 
				 * start the configuration activity
				 */	
					Intent intent= new Intent(getApplicationContext(),ConfigurationActivity.class);
					startActivity(intent);
					finish();

				}
			}); 
	}

}
