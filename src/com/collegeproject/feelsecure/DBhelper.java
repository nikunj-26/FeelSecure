package com.collegeproject.feelsecure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBhelper extends SQLiteOpenHelper {
	
	
	public static final String Database_name="Feelsecure.db";
//	public  final String identifer = "id";
	public final String Table_name="contacts";
	 public static final String KEY_ROWID = "id";
	public DBhelper(Context context) {
		super(context, Database_name, null, 1);
		// TODO Auto-generated constructor stub
	}

	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		/* creating tabel to record the phone on which it will send the message.
		 * 
		 * 
		 * 
		 * 
		 * */
		//Log.d("creating table", null);
		
		db.execSQL("CREATE TABLE contacts (id integer,phonenumber text);");
	Log.w(Database_name, "table created");
	
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL("DROP TABLE IF EXISTS contacts");
		onCreate(db);
		
	}

public boolean insertcontact(int id,String phonenum){
	
	/* Inserting data into the database/table contacts
	 * */
	SQLiteDatabase db= this.getWritableDatabase();
	ContentValues content= new ContentValues();
	content.put("id", id);
	content.put("phonenumber", phonenum);
	db.insert("contacts",null,content);
	return true;
	}

	public Cursor getdata(int id){
		
		SQLiteDatabase getdb = this.getReadableDatabase();
		
		Cursor result=getdb.rawQuery("SELECT phonenumber from contacts where id="+id+";", null);
		
		
		return result;
		
		
	}
public boolean updatecontact(int ide ,String Phonenumber){
			
			
			SQLiteDatabase  db= this.getWritableDatabase();
			ContentValues content= new ContentValues();
			//content.put("id", id);
			content.put("phonenumber", Phonenumber);
			int check=db.update(Table_name, content, KEY_ROWID+"="+ide, null);
			if(check>0){
		//	db.rawQuery("UPDATE contacts SET phonenumber ='"+Phonenumber+"' Where id="+id+";",null);
			
			return true;
			}
			return false;
	
	
		}
}
