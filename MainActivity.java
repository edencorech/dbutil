package com.yuval.dbutil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyDBHelp db=null;
    private List<String> table_data=null;
    private MyAdapter adapter=null;
    private static final String CMD_SELECT="SELECT";
    ListView lstResults=null;
    Button btnExec=null;
    private EditText edtCommand=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            db=new MyDBHelp(this);
        }
        catch(Exception e)
        {
            db=null;
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        }

        table_data=new ArrayList<String>();
        adapter=new MyAdapter();
        lstResults=findViewById(R.id.lst_results);
        lstResults.setAdapter(adapter);
        edtCommand=findViewById(R.id.edt_command);
        btnExec=findViewById(R.id.btn_exec_command);
        btnExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execCommand();
            }
        });

    }

    private void execCommand() {
        if (db==null) return;
        String command=edtCommand.getText().toString();
        if (command==null) return;
        if (command=="") return;
        if (isSelect(command))
            doQuery(command);
        else
            doExec(command);
    }

    private void doQuery(String command) {
        if (db==null) return;
        List<String> list=db.doQuery(command);
        if (list==null) {
            Toast.makeText(this, "ERROR in query", Toast.LENGTH_LONG).show();
            table_data.clear();
        }
        else
            table_data=list;
        adapter.notifyDataSetChanged();
    }

    private void doExec(String command) {
        String msg=db.execSQL(command);
        if (msg=="")
            msg="SUCCESS";
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private boolean isSelect(String command) {
        return (command.toUpperCase().substring(0,CMD_SELECT.length()+1)==CMD_SELECT+" ");
    }

    private class MyAdapter extends ArrayAdapter<String>
    {
        public MyAdapter()
        {
            super(getApplicationContext(),R.layout.item,table_data);
        }

        @Override
        public String getItem(int position) {
            if (table_data==null) return null;
            if ((position<0)||(position>=table_data.size()))
                return null;
            return table_data.get(position);
        }

        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            if (convertView==null)
            {
                LayoutInflater mInflater=LayoutInflater.from(getApplicationContext());
                convertView=mInflater.inflate(R.layout.item,parent,false);
            }
            ((TextView)convertView).setText(getItem(position));
            return convertView;
        }
    }
}