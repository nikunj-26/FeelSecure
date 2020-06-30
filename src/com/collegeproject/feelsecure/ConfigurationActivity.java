package com.collegeproject.feelsecure;



import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;


public class ConfigurationActivity extends Activity {

EditText phone1,phone2,phone3;
TextView displayname1,displayname2,displayname3;
int flag=0;
private DBhelper mydb;
SharedPreferences userprefence;
Button button1;
String userphonenumber="";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		
		//finding the buton in the view
		 userprefence=getApplicationContext().getSharedPreferences("FeelSecurePref",MODE_PRIVATE);
		 mydb= new DBhelper(getApplicationContext());
		button1=(Button) findViewById(R.id.button1);
		
		button1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				userprefence.edit().putBoolean("FirstTime",false).commit();
				Intent intentservice=new Intent(getApplicationContext(),Mainservice.class);
				startService(intentservice);
			
				Toast.makeText(getApplicationContext(), "Thanks For Using Feelsafe", Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable(){

					@Override
					public void run() {
						/*
						 *  After the splash image has been shown now start the activity through intent 
						 *  which shows what to do and how to configure the application
						 *  start the activity-> BasicInfoActivity
						 */
					finish();
					}},2500);
			}	
			
			
		});
		
		// finding the edittextbox
		phone1= (EditText) findViewById(R.id.phonenumberinput1);
		phone2= (EditText) findViewById(R.id.phonenumberinput2);
		phone3= (EditText) findViewById(R.id.phonenumberinput3);
		
		
		
		// finding all the textviews
		 displayname1=(TextView) findViewById(R.id.displayname1);
		 displayname2=(TextView) findViewById(R.id.displayname2);
		 displayname3=(TextView) findViewById(R.id.displayname3);
		
		//setting onlonhold listner to the text box
		
		phone1.setOnLongClickListener(new OnLongClickListener(){
			public boolean onLongClick(View arg0) {
				if(flag==0){
				Intent phonebookintent=  new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
			    phonebookintent.setType(Phone.CONTENT_TYPE);
				startActivityForResult(phonebookintent,1);
				flag=1;
				}else{
						Toast.makeText(getApplicationContext(), "Select the second Field", Toast.LENGTH_LONG).show();
				}
				return true;

				}			

				});	
		
		//If condition to make sure that first edit text contains dataand directly second edit text is not selected.
		
		phone2.setOnLongClickListener(new OnLongClickListener(){
			public boolean onLongClick(View arg0) {
				
				/*repeating the same procedure
				 * as we did for the first time to get the contack info.
				 */
				if(flag==1){
				Intent getcontact2= new Intent(Intent.ACTION_PICK,Uri.parse("content://contacts"));
				getcontact2.setType(Phone.CONTENT_TYPE);
				startActivityForResult(getcontact2,2);
				
				
				flag=2;
				} else{
					
					Toast.makeText(getApplicationContext(), "please select first field ", Toast.LENGTH_LONG).show();
				}
				
				
				return true;
			}
			
		});
		/*
		 * 
		 * Setting onclick listner on the 3rd edittext
		 */
				
		
		phone3.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				
				/*repeating the same procedure
				 * as we did for the first time to get the contack info.
				 */
				if(flag==2){
				Intent getcontact2= new Intent(Intent.ACTION_PICK,Uri.parse("content://contacts"));
				getcontact2.setType(Phone.CONTENT_TYPE);
				startActivityForResult(getcontact2,3);
				flag=0;
				}else {
					
					Toast.makeText(getApplicationContext(), "Please Select the second Field ", Toast.LENGTH_LONG).show();
				}
				return true;
			}
			
		}); 
		
	}
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 * 
	 * this to get phone number of first person.
	 * 
	 */
	public void onActivityResult(int reqcode,int rescode,Intent data){
		if(reqcode==1){
		if(rescode==RESULT_OK){
			
			Uri phonenumber=data.getData();
	            // We only need the NUMBER column, because there will be only one row in the result
	            String[] projection = {Phone.NUMBER,Phone.DISPLAY_NAME};

	            // Perform the query on the contact to get the NUMBER column
	            // We don't need a selection or sort order (there's only one result for the given URI)
	            // CAUTION: The query() method should be called from a separate thread to avoid blocking
	            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
	            // Consider using CursorLoader to perform the query.
	            Cursor cursor = getContentResolver().query(phonenumber, projection, null, null, null);
	            cursor.moveToFirst();
	            // Retrieve the phone number from the NUMBER column
	            int column = cursor.getColumnIndex(Phone.NUMBER);
	            int name=cursor.getColumnIndex(Phone.DISPLAY_NAME);
	            
	            
	          //  userphonenumber = cursor.getString(column);
	           
	            displayname1.setText(cursor.getString(name));
	            phone1.setText(cursor.getString(column));
	           boolean testbool=userprefence.getBoolean("FirstTime",true);
	            if(testbool){
	            	boolean datainserted = mydb.insertcontact(1,phone1.getText().toString());
				
	            	if(datainserted){
	            		Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_LONG).show();
	            	}else{
					Toast.makeText(getApplicationContext(), "Insertion failed", Toast.LENGTH_LONG).show();
					
				}
	            	 
	            }
	            else{
	            
	            	 boolean datainserted = mydb.updatecontact(1, phone1.getText().toString());
	            	  
	            	  if(datainserted){
	      				Toast.makeText(getApplicationContext(), "data Updated", Toast.LENGTH_LONG).show();
	      				}else{
	      					Toast.makeText(getApplicationContext(), "Insertion failed", Toast.LENGTH_LONG).show();
	      					
	      				}
			
			 }//end of userprefpart
			}
		}// end of first IF condition which is for req code=1
		
		
		/*
		 * as the Same onActivityresult function will be called for every edit text Intent send 
		 * so we change the request code to know which edittext has sent the request and process it accordingly.
		 * 
		 */
		if(reqcode==2){
			if(rescode==RESULT_OK){
				
				Uri phonenumber=data.getData();
		            // We only need the NUMBER column, because there will be only one row in the result
		            String[] projection = {Phone.NUMBER,Phone.DISPLAY_NAME};

		            // Perform the query on the contact to get the NUMBER column
		            // We don't need a selection or sort order (there's only one result for the given URI)
		            // CAUTION: The query() method should be called from a separate thread to avoid blocking
		            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
		            // Consider using CursorLoader to perform the query.
		            Cursor cursor = getContentResolver().query(phonenumber, projection, null, null, null);
		            cursor.moveToFirst();
		            // Retrieve the phone number from the NUMBER column
		            int column = cursor.getColumnIndex(Phone.NUMBER);
		            int name=cursor.getColumnIndex(Phone.DISPLAY_NAME);
		            
		            
		            userphonenumber = cursor.getString(column);
		           
		            displayname2.setText(cursor.getString(name));
		            phone2.setText(userphonenumber);
		            boolean testbool=userprefence.getBoolean("FirstTime",true);
		            if(testbool){
		            	boolean datainserted = mydb.insertcontact(2,phone2.getText().toString());
					
		            	if(datainserted){
		            		Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_LONG).show();
		            	}else{
						Toast.makeText(getApplicationContext(), "Insertion failed", Toast.LENGTH_LONG).show();
						
					}
		            	 
		            }
		            else{
		            
		            	 boolean datainserted = mydb.updatecontact(2, phone2.getText().toString());
		            	  
		            	  if(datainserted){
		      				Toast.makeText(getApplicationContext(), "data Updated", Toast.LENGTH_LONG).show();
		      				}else{
		      					Toast.makeText(getApplicationContext(), "Insertion failed", Toast.LENGTH_LONG).show();
		      					
		      				}
				
				 }//end of userprefpart
				
				 }
		
			}
		
		
		/*
		 * now Repeating the code for 3rd edittext.
		 */
		
		if(reqcode==3){
			if(rescode==RESULT_OK){
				
				Uri phonenumber=data.getData();
		            // We only need the NUMBER column, because there will be only one row in the result
		            String[] projection = {Phone.NUMBER,Phone.DISPLAY_NAME};

		            // Perform the query on the contact to get the NUMBER column
		            // We don't need a selection or sort order (there's only one result for the given URI)
		            // CAUTION: The query() method should be called from a separate thread to avoid blocking
		            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
		            // Consider using CursorLoader to perform the query.
		            Cursor cursor = getContentResolver().query(phonenumber, projection, null, null, null);
		            cursor.moveToFirst();
		            // Retrieve the phone number from the NUMBER column
		            int column = cursor.getColumnIndex(Phone.NUMBER);
		            int name=cursor.getColumnIndex(Phone.DISPLAY_NAME);
		            
		            
		            userphonenumber = cursor.getString(column);
		           
		            displayname3.setText(cursor.getString(name));
		            phone3.setText(userphonenumber);
		            
		            boolean testbool=userprefence.getBoolean("FirstTime",true);
		            if(testbool){
		            	boolean datainserted = mydb.insertcontact(3,phone3.getText().toString());
					
		            	if(datainserted){
		            		Toast.makeText(getApplicationContext(), "data inserted", Toast.LENGTH_LONG).show();
		            	}else{
						Toast.makeText(getApplicationContext(), "Insertion failed", Toast.LENGTH_LONG).show();
						
					}
		            	 
		            }
		            else{
		            
		            	 boolean datainserted = mydb.updatecontact(3, phone3.getText().toString());
		            	  
		            	  if(datainserted){
		      				Toast.makeText(getApplicationContext(), "data Updated", Toast.LENGTH_LONG).show();
		      				}else{
		      					Toast.makeText(getApplicationContext(), "Insertion failed", Toast.LENGTH_LONG).show();
		      					
		      				}
				
				 }//end of userprefpart

				 }// end of Inner If condition
		
		
			}//End of Outer If Condition.
	
	}// end of onactivityresult function.
	
}
