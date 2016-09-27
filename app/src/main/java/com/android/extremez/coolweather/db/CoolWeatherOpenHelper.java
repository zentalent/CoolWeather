package com.android.extremez.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tingzong on 2016/9/27.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper{

        public static final String CREATE_PROVINCE = "create table Province ("
                + "id integer primary key autoincrement,"
                + "province_name text,"
                + "province_code text)";

        public static final String CREATE_CITY = "create table City ("
                + "id integer primary key autoincremen, "
                + "city_name text, "
                + "city_code text, "
                + "Provice_id integer)";

        public static final String CREATE_COUNTY = "create table County ("
                + "id integer primary key autoincremet, "
                + "county_name text, "
                + "county_code text, "
                + "city_id integer)";

        public CoolWeatherOpenHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
            super(context ,name,factory,version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_PROVINCE);
            db.execSQL(CREATE_CITY);
            db.execSQL(CREATE_COUNTY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

        }
}
