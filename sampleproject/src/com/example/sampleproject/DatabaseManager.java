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
							TABLE_ROW_CTIME + " long," +
							TABLE_ROW_UTIME + " long" +
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
	
	public void selectRules(String ipAddress, String website){
		Cursor c=db.rawQuery("select * from " + TABLE_NAME,null);
	}
	
}
