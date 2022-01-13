package com.example.mobiledictionary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledictionary.English.EngViet;

import java.util.ArrayList;

public class Setting extends AppCompatActivity  {

    RadioButton anhAnh, anhMy;
    String accentSelection;
    private boolean anhAnhChecked = true, anhMyChecked = true;
    public static EngViet engViet = new EngViet();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ArrayList<String> arr24Hour;
    ArrayList<String> arrMin;
    Spinner spinner24Hour;
    Spinner spinnerMin;
    public int hourSetting;
    public int minSetting;

    public Setting () {

    }
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.settinglayout);
        sharedPref = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        arr24Hour = new ArrayList<String>();
        arrMin = new ArrayList<String>();
        for (int i = 0; i < 25; i ++) {
            String element = String.valueOf(i);
            if (i < 10) element = "0" + element;
            Log.d("phần tử thứ:",element);
            arr24Hour.add(element);
        }

        for (int i = 0; i < 61; i ++) {
            String element = String.valueOf(i);
            if (i < 10) element = "0" + element;
            Log.d("phần tử thứ:",element);
            arrMin.add(element);
        }
        hourSetting = sharedPref.getInt("hourSetting", 0);
        minSetting = sharedPref.getInt("minSetting", 0);

        anhAnh = findViewById(R.id.en);
        anhMy = findViewById(R.id.en_US);
        spinner24Hour = findViewById(R.id.hour24);
        spinnerMin = findViewById(R.id.minute);

        if (sharedPref.getBoolean("anhAnhSelected",false) == true) {
            anhAnh.setChecked(true);
            anhMy.setChecked(false);
        }

        anhAnh.setOnCheckedChangeListener(listenerRadio);
        anhMy.setOnCheckedChangeListener(listenerRadio);

        //phần cài đặt thời gian
        //cài đặt giờ
        ArrayAdapter<String> spinAdapter24hour = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,arr24Hour);
        spinAdapter24hour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner24Hour.setAdapter(spinAdapter24hour);
        spinner24Hour.setSelection(hourSetting);
        spinner24Hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("hourSet", arr24Hour.get(position));
                editor.putInt("hourPos", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("không cái gì đc chọn", "");
            }
        });
        //cài đặt phút
        ArrayAdapter<String> spinAdapterMin = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,arrMin);
        spinAdapterMin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMin.setAdapter(spinAdapterMin);
        spinnerMin.setSelection(minSetting);
        spinnerMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("minSet", arrMin.get(position));
                editor.putInt("minPos", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("không cái gì đc chọn", "");
            }
        });
    }

    CompoundButton.OnCheckedChangeListener listenerRadio
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (anhAnh.isChecked()) {
                accentSelection = "English";
                editor.putString("accent", accentSelection);
                editor.apply();
            }

            else if (anhMy.isChecked()) {
                accentSelection = "EngUS";
                editor.putString("accent", accentSelection);
                editor.apply();
            }
        }
    };
}


//        if (this.anhAnhChecked == flase) {
//            anhAnh.setChecked(true);
//        }
//        else {
//            anhMy.setChecked(true);
//        }


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }