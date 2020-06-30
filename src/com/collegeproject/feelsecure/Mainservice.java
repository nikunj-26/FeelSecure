package com.collegeproject.feelsecure;

import java.io.IOException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Mainservice extends Service implements SensorListener, LocationListener {
	SensorManager mysensimanager;
	Sensor mysensor;
	int i=0;
	float x_last = 0;
	float y_last;
	float z_last;
	long lastupdate = 0;
	boolean min=false,max=false;
	float x_axis;
	float y_axis;
	float z_axis;

	private MediaRecorder mRecorder = null;
	boolean isGPSEnabled = false;
	 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    // flag for GPS status
    boolean canGetLocation = false;
 
    Location location; // location
   double latitude; // latitude
   double longitude; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
    LocationManager locationmanage;
	
    Geocoder geocoder;
	
	DBhelper mydb;
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public int onStartCommand(Intent intent, int flags, int startId) {
	    // We want this service to continue running until it is explicitly
	    // stopped, so return sticky.
		mydb=new DBhelper(getApplicationContext());
		mysensimanager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mysensimanager.registerListener(this,SensorManager.SENSOR_ACCELEROMETER,
				SensorManager.SENSOR_DELAY_GAME);
		
		//mysensor=mysensimanager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		if (mysensimanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null){
				Toast.makeText(getApplicationContext(),"Needed Accelerometer for this application to work",Toast.LENGTH_LONG).show();
		}
		
		//Toast.makeText(getApplicationContext(), "Service Started",Toast.LENGTH_LONG).show();
	    return START_STICKY;
	    
	}




	@Override
	public void onAccuracyChanged(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onSensorChanged(int arg0, float[] arg1) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				float abcd = (arg1[0]);
			//	Txt1.setText("hello"+abcd);
				
				long CurrentTime=System.currentTimeMillis();
				
				if(CurrentTime-lastupdate>1000){
					long difftime=CurrentTime-lastupdate;
					
					lastupdate=CurrentTime;
					
					 x_axis=arg1[0];
					 y_axis=arg1[1];
					 z_axis=arg1[3];
					
					float speed=Math.abs((x_axis+y_axis+z_axis)-x_last-y_last-z_last)/ difftime * 10000;
					
				//	speedtext.setText("speed"+speed);
					if(speed>450){
						Toast.makeText(getApplicationContext(), "Shake", Toast.LENGTH_LONG).show();
						
					
						
						StartRecording();
						GetLocation();
						
						Cursor res=mydb.getdata(1);
						
						res.moveToFirst();
						
						String number =res.getString(res.getColumnIndex("phonenumber"));
						//SmsManager sendmessage=SmsManager.getDefault();
						
						//sendmessage.sendTextMessage(number, null, "android sending message test",null, null);
						Toast.makeText(getApplicationContext(), number, Toast.LENGTH_LONG).show();
					}
					x_last=x_axis;
					y_last=y_axis;
					z_last=z_axis;
					
					
					double aaa=Math.round(Math.sqrt(Math.pow(x_axis, 2)
                            +Math.pow(y_axis, 2)
                            +Math.pow(z_axis, 2)));
					
					
					if (aaa<=6.0) {
						min=true;
						//mintime=System.currentTimeMillis();
					}

					if (min==true) {
						i++;
						if(aaa>=16) {
							max=true;
						}
					}

					if (min==true && max==true) {
						//Toast.makeText(this,"FALL DETECTED!!!!!" ,Toast.LENGTH_LONG).show();

						StartRecording();
						GetLocation();
						Cursor res=mydb.getdata(1);
						
						res.moveToFirst();
						while(res.moveToNext()){
						String number =res.getString(res.getColumnIndex("phonenumber"));
						
						Toast.makeText(getApplicationContext(), "FallDetected", Toast.LENGTH_LONG).show();
						//SmsManager sendmessage=SmsManager.getDefault();
						//sendmessage.sendTextMessage(number, null, "android sending message test",null, null);
						}
						i=0;
						min=false;
						max=false;
						
											}

					if (i>4) {
						i=0;
						min=false;
						max=false;
					}
		
				}
		}
	
	
	
	
	private void StartRecording() {
		// TODO Auto-generated method stub
		
		String mypath=Environment.getExternalStorageDirectory().getAbsolutePath();
		mypath += "/FeelsecureVoice.3gp";
       // Toast.makeText(getApplicationContext(),mypath,Toast.LENGTH_LONG).show();
		mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mypath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        
        try {
            mRecorder.prepare();
        } catch (IOException e) {
           // Log.e(LOG_TAG, "prepare() failed");
        }
        Toast.makeText(getApplicationContext(), "starting Recording", Toast.LENGTH_LONG).show();
        mRecorder.start();
       /* new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mRecorder.stop();
		        mRecorder.release();
		        mRecorder = null;
		    
		        Toast.makeText(getApplicationContext(), "Stoping recording", Toast.LENGTH_LONG).show();
				
			}	
        	
        },10000);
        */
        
        
        Runnable runob=new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
		                try{
		                	Thread.sleep(10000);
		                	mRecorder.stop();
		    		        mRecorder.release();
		    		        mRecorder = null;
		    		    
		    		        Toast.makeText(getApplicationContext(), "Stoping recording", Toast.LENGTH_LONG).show();
		                }catch (Exception e){    }
		                
		                
				}
        };
			
			
			new Handler().post(runob);
		}

	public void GetLocation(){
		
		locationmanage = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	
		
		isNetworkEnabled = locationmanage
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
		if (isNetworkEnabled)locationmanage.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000*60*1,1,this);
		
		Runnable runob=new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isNetworkEnabled) {
		            if (locationmanage != null) {
		                location = locationmanage.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		                
		                try{
		                	Thread.sleep(2000);
		                }catch (Exception e){    }
		                
		                if (location != null) {
		                    latitude = location.getLatitude();
		                    longitude = location.getLongitude();
		                }
		            }
		        }
				Toast.makeText(getApplicationContext(),"Latitude"+latitude+"Longitude"+longitude, Toast.LENGTH_LONG).show();
			}
			
		};
		
		new Handler().post(runob);
				
	}

@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
		
		
	

}
