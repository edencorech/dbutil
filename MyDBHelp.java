package com.yuval.dbutil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelp extends SQLiteOpenHelper {
    private static final int DATABASEVERSION=1;
    private static final String DBNAME="test.db";

    public MyDBHelp(Context context)
    {
        super(context,DBNAME,null,DATABASEVERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String execSQL(String command)
    {
        String ret="";
        SQLiteDatabase db=this.getWritableDatabase();
        try
        {
            db.execSQL(command);
        }
        catch(Exception e)
        {
            ret=e.getMessage();
        }
        db.close();
        return ret;
    }

    public List<String> doQuery(String command)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=null;
        try
        {
            c=db.rawQuery(command,null);
        }
        catch(Exception e)
        {
            c=null;
        }
        if (c==null) return null;
        List<String> ret=new ArrayList<String>();
        String str;
        int i;
        for(str="",i=0;i<c.getColumnCount();str=str+c.getColumnName(i),i++);
        ret.add(str);
        if (c.moveToFirst())
        {
            do {
                for(i=0,str="";i<c.getColumnCount();str=str+c.getString(i),i++);
                ret.add(str);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return ret;
    }
}
