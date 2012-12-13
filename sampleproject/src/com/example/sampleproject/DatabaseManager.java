package com.example.sampleproject;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
	Context context;
	private SQLiteDatabase db;
	private final String DB_NAME="database_name";
	private final int DB_VERSION=5;

	private final String TABLE_NAME="database_table";
	private final String TABLE_ROW_IP="table_row_ip";
	//primary key set as auto increment
	private final String TABLE_ROW_ID="id";

	private final String TABLE_ROW_Site="table_row_two";
	private final String TABLE_ROW_Action="table_row_rule";
	private final String TABLE_ROW_Port="table_row_port";
	//*****Initialise CTIME and UTIME here itself***************
	private final String TABLE_ROW_CTIME="table_row_ctime";
	private final String TABLE_ROW_UTIME="table_row_utime";
	public static final String TAG="delete";
	//public static final String RETRIEVE="first value";
	//public static final String GET="second value";

	public DatabaseManager(Context context)
	{
		this.context=context;
		CustomSQLiteOpenHelper helper=new CustomSQLiteOpenHelper(context);
		this.db=helper.getWritableDatabase();
	}

	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
	{
		public CustomSQLiteOpenHelper(Context context)
		{
			super(context,DB_NAME,null,DB_VERSION);
		}
		public void onCreate(SQLiteDatabase db)
		{
			String newQuery="create table " + 
					TABLE_NAME + 
					" (" + 
					TABLE_ROW_ID + " integer primary key autoincrement not null," +
					TABLE_ROW_IP + " text," + 
					TABLE_ROW_Site + " text," +
					TABLE_ROW_Action + " text," +
					TABLE_ROW_Port + " integer," + 
					TABLE_ROW_CTIME + " text," +
					TABLE_ROW_UTIME + " text" +
					");";
			db.execSQL(newQuery);
			String select="select * from " + TABLE_NAME;
			db.execSQL(select);
		}
		public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
			onCreate(db);
		}
	}	

	public ArrayList<Rule> selectRules(Rule searchRule){

		ArrayList<Rule> output = new ArrayList<Rule>();
		String ipAddress = searchRule.getIpAddress();
		String websiteAddr = searchRule.getWebsiteAddress();

		String query = "SELECT * FROM " + TABLE_NAME + " where " + TABLE_ROW_IP + "=" + ipAddress + " AND " + TABLE_ROW_Site + "=" + websiteAddr;
		

		Cursor c=db.rawQuery(query  ,null);
		if(c!=null){
			if(c.moveToFirst()){
				do{
					Rule currentRule = new Rule();
					currentRule.setIpAddress(c.getString(c.getColumnIndex(TABLE_ROW_IP)));
					currentRule.setWebsiteAddress(c.getString(c.getColumnIndex(TABLE_ROW_Site)));
					currentRule.setAction(c.getString(c.getColumnIndex(TABLE_ROW_Action)));
					output.add(currentRule);
				}while(c.moveToNext());
			}
		}	
		return output;
	}
	public boolean addRule(Rule obj)
	{
		//int id=obj.getId();
		
		String address=obj.getIpAddress();
		String site=obj.getWebsiteAddress();
		String action=obj.getAction();
		String ctime=obj.getCreatedTime();
		String utime=obj.getUpdateTime();
		int port=obj.getPort();
		
		ContentValues values=new ContentValues();
		if(address!=null)
			values.put(TABLE_ROW_IP, address);
		else 
			return false;
		if(site!=null)
			values.put(TABLE_ROW_Site,site);
		else
			return false;
		if(action!=null)
			values.put(TABLE_ROW_Action, action);
		else
			return false;
		if(ctime==null)
			values.put(TABLE_ROW_CTIME, "");
		else
			return false;
		if(utime==null)
			values.put(TABLE_ROW_UTIME, "");
		else
			return false;
		if(Integer.toString(port)==null)
			values.put(TABLE_ROW_Port, 0);
		else	
			return false;
		try
		{
			db.insert(TABLE_NAME,null,values);
			 return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public boolean deleteRule(Rule obj)
	{
		long id=obj.getId();
		try
		{
			db.delete(TABLE_NAME, TABLE_ROW_ID + "=" + id,null);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public ArrayList<Rule> getAllRows()
	{
		Cursor cursor;
		ArrayList<Rule> rule=new ArrayList();
		try
		{
				cursor=db.query(
				TABLE_NAME, new String[]{TABLE_ROW_ID,TABLE_ROW_IP,TABLE_ROW_Site,TABLE_ROW_Action,TABLE_ROW_Port,TABLE_ROW_CTIME,TABLE_ROW_UTIME}, 
					null, null, null, null, null);
			cursor.moveToFirst();
			if(!cursor.isAfterLast())
			{
				do
				{
					Rule currentObj=new Rule();
					currentObj.setId(Integer.parseInt(cursor.getString(0)));
					currentObj.setIpAddress(cursor.getString(1));
					currentObj.setWebsiteAddress(cursor.getString(2));
					currentObj.setAction(cursor.getString(3));
					currentObj.setPort(Integer.parseInt(cursor.getString(4)));
					currentObj.setCreatedTime(cursor.getString(5));
					currentObj.setUpdateTime(cursor.getString(6));
					
					rule.add(currentObj);
					
				}while(cursor.moveToNext());			
			}
		}
		catch(Exception e)
		{
			return null;
		}
		return rule;
	}
	public boolean updateRule(Rule obj)
	{
		long id=obj.getId();
		String address=obj.getIpAddress();
		String site=obj.getWebsiteAddress();
		String action=obj.getAction();
		String ctime=obj.getCreatedTime();
		String utime=obj.getUpdateTime();
		int port =obj.getPort();
		ContentValues content=new ContentValues();
		content.put(TABLE_ROW_IP, address);
		content.put(TABLE_ROW_Site, site);
		content.put(TABLE_ROW_Action, action);
		content.put(TABLE_ROW_CTIME, ctime);
		content.put(TABLE_ROW_UTIME,utime);
		content.put(TABLE_ROW_Port, 0);
		
		//String where=""
		try{
		int num_rows_updated=db.update(TABLE_NAME, content, "TABLE_ROW_ID=" + id, null);
		return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
