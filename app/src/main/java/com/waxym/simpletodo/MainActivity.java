package com.waxym.simpletodo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton floatingActionButton;

    ArrayList<String> toDoList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        toDoList = retrieveList();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, toDoList);
        listView.setAdapter(arrayAdapter);

        floatingActionButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeItem(position);
                return false;
            }
        });

    }

    private void openDialog(){
        final EditText input = new EditText(MainActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ajout d'un item")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String items = input.getText().toString();
                        toDoList.add(items);
                        saveList();
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setView(input);
        alert.show();
    }

    private void removeItem(int position){
        toDoList.remove(position);
        saveList();
        arrayAdapter.notifyDataSetChanged();
    }

    private void saveList(){
        Gson gson = new Gson();
        String myListInJson = gson.toJson(toDoList);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("toDoList", myListInJson);
        editor.apply();
    }

    private ArrayList<String> retrieveList(){
        Gson gson = new Gson();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        String myListInJson = sharedPref.getString("toDoList", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> list = gson.fromJson(myListInJson, type);
        return list != null ? list : new ArrayList<String>();
    }
}
