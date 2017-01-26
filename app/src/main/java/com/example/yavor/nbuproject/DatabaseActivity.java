package com.example.yavor.nbuproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.yavor.nbuproject.Data.DataBaseClass;

public class DatabaseActivity extends BaseActivity {

    private ListView list_lv;
    private EditText col1_ed;
    private EditText col2_ed;
    private Button sub_btn;
    private Button ref_btn;
    private DataBaseClass db;

    private ArrayList<String> collist_1;
    private ArrayList<String> collist_2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        collist_1 = new ArrayList<String>();
        collist_2 = new ArrayList<String>();
        items();
        getData();
    }

    private void items() {
        sub_btn = (Button) findViewById(R.id.submit_btn);
        ref_btn = (Button) findViewById(R.id.refresh_btn);
        col1_ed = (EditText) findViewById(R.id.ed1);
        col2_ed = (EditText) findViewById(R.id.ed2);
        list_lv = (ListView) findViewById(R.id.dblist);

        ref_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getData();
            }
        });

        sub_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    protected void submitData() {
        String a = col1_ed.getText().toString();
        String b = col2_ed.getText().toString();

        db = new DataBaseClass(this);
        long num;
        try {
            db.open();
            num = db.insertmaster(a, b);
            db.close();
        } catch (SQLException e) {
            num = -5;
        } finally {
            getData();
        }
        if (num > 0)
            Toast.makeText(this, "Row number: " + num, Toast.LENGTH_SHORT).show();
        else if (num == -1)
            Toast.makeText(this, "Error Duplicate value", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Error while inserting", Toast.LENGTH_SHORT).show();
    }

    public void getData() {
        collist_1.clear();
        collist_2.clear();

        db = new DataBaseClass(this);
        try {
            db.open();
            Cursor cur = db.getAllTitles();
            while (cur.moveToNext()) {
                String valueofcol1 = cur.getString(1);
                String valueofcol2 = cur.getString(2);
//              Log.e("---****---", "***********   col 1 = " + valueofcol1);
//              Log.e("---****---", "***********   col 2 = " + valueofcol2);

                collist_1.add(valueofcol1);
                collist_2.add(valueofcol2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        printList();
        setDataIntoList();
    }

    private void printList() {
        for (int i = 0; i < collist_1.size(); i++) {
            Log.e("***************",
                    collist_1.get(i) + " --- " + collist_2.get(i));
        }
    }

    private void setDataIntoList() {

        // create the list item mapping
        String[] from = new String[] { "col_1", "col_2" };
        int[] to = new int[] { R.id.col1tv, R.id.col2tv };

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < collist_1.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("col_1", collist_1.get(i));
            map.put("col_2", collist_2.get(i));
            fillMaps.add(map);
        }

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
                R.layout.custom, from, to);
        list_lv.setAdapter(adapter);
    }
}
