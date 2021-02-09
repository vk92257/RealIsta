package com.example.myapplication.modelsprofile.models_selectoptions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.pojo.SelectItem;
import com.example.myapplication.utils.ConstantString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Languages_selectActivity extends AppCompatActivity {
    private TextView title_tool;
    private ListView list_selecteddata;
    private String[] data;
    private ArrayList<String> checkdata;
    private String titletxt;
    private int sendcode;
    private Set<String> listcount;

    private SelectItem[] noofdata;
    private static String TAG = "Languages_selectActivity";


    // public   Languages_selectActivity(ArrayList<String> listofdata){
//        this.listofdata = listofdata;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages_select);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        listcount  = new HashSet<String>();
        Log.d(TAG, "onCreate: data check-> " + (ArrayList<String>) getIntent().getSerializableExtra("checkdata"));
        if ((ArrayList<String>) getIntent().getSerializableExtra("checkdata") != null) {
            checkdata = (ArrayList<String>) getIntent().getSerializableExtra("checkdata");
//            checkdata = getIntent().getStringArrayListExtra("checkdata");
            Log.d(TAG, "onCreate: checkdata is not null " + checkdata);
        }
        data = getIntent().getStringArrayExtra("data");
        titletxt = getIntent().getStringExtra(ConstantString.SELECT_TOP_TXT);
        sendcode = getIntent().getIntExtra(ConstantString.CODETEXT, 1001);
        Log.d(TAG, "onCreate: list in -> " + data + " sendcode " + sendcode);
        initvalues();
        setdata();
    }

    private void initvalues() {
        title_tool = findViewById(R.id.title);
        list_selecteddata = findViewById(R.id.list);
        title_tool.setText(titletxt);

    }


    private void setdata() {
        list_selecteddata.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list_selecteddata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                SelectItem selectItem = (SelectItem) list_selecteddata.getItemAtPosition(position);
                selectItem.setActive(!currentCheck);
            }
        });
        noofdata = new SelectItem[data.length];
        for (int i = 0; i < data.length; i++) {
            Log.d(TAG, "setdata: " + data[i]);
            String lang = data[i];
            noofdata[i] = new SelectItem(lang);
        }
        Log.d(TAG, "setdata: " + Arrays.toString(noofdata));
        ArrayAdapter<SelectItem> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, noofdata);

        list_selecteddata.setAdapter(arrayAdapter);
        for (int i = 0; i < noofdata.length; i++) {
            this.list_selecteddata.setItemChecked(i, !noofdata[i].getActive());
        }
        if(checkdata != null){
            checked();
        }
//        if (checkdata != null) {
//            checkeddata();
//        } else {
//            for (int i = 0; i < noofdata.length; i++) {
//                this.list_selecteddata.setItemChecked(i, !noofdata[i].getActive());
//            }
//        }

    }

    public void checked(){
        for (int j = 0; j < checkdata.size(); j++) {
            String data = checkdata.get(j);
            Log.d(TAG, "checkeddata: data-> " + data);
            for (int i = j; i < noofdata.length; i++) {
                Log.d(TAG, "checkeddata: listcountdata-> "+listcount);
                if (noofdata[i].getDataname().equals(data))  {
                    listcount.add(data);
                    Log.d(TAG, "checkeddata: in loop -> " + noofdata[i].getDataname());
                    this.list_selecteddata.setItemChecked(i, noofdata[i].getActive());

                }

            }
        }
    }

//    public void checkeddata() {
//        for (int j = 0; j < checkdata.size(); j++) {
//            String data = checkdata.get(j);
//            Log.d(TAG, "checkeddata: data-> " + data);
//            for (int i = j; i < noofdata.length; i++) {
//                Log.d(TAG, "checkeddata: listcountdata-> "+listcount);
//                   if (noofdata[i].getDataname().equals(data))  {
//                       listcount.add(data);
//                       Log.d(TAG, "checkeddata: in loop -> " + noofdata[i].getDataname());
//                       this.list_selecteddata.setItemChecked(i, noofdata[i].getActive());
//
//                   }
//                else if(listcount.contains(data)){
//                   this.list_selecteddata.setItemChecked(i, noofdata[i].getActive());
//               }
//                else{
//                       this.list_selecteddata.setItemChecked(i, !noofdata[i].getActive());
//                   }
//            }
//        }
//
//    }

    public void save(View view) {
        ArrayList<String> listofdata = new ArrayList<>();
        SparseBooleanArray sp = list_selecteddata.getCheckedItemPositions();
        Log.d(TAG, "save: list_selecteddata-> "+list_selecteddata.getCheckedItemPositions());
        for(int i=0;i<sp.size();i++){
            if(sp.valueAt(i)){
                SelectItem user= (SelectItem) list_selecteddata.getItemAtPosition(i);
                Log.d(TAG, "save: data-> "+i+" -> "+user.getDataname());
                listofdata.add(user.getDataname());
            }
        }

//        Log.d(TAG, "save: list_selecteddata-> " + list_selecteddata.getCheckedItemPositions() +
//                "\n listcount " + listcount+" listcountsize-> "+listcount.size());
//        SparseBooleanArray sp = list_selecteddata.getCheckedItemPositions();
//        for (int i = 0; i < listcount.size(); i++) {
//            Log.d(TAG, "save: sp.valueAt -> "+sp.valueAt(listcount.get(i))+" listcount.get() "+listcount.get(i));
//            if (sp.valueAt(listcount.get(i))) {
//                SelectItem user = (SelectItem) list_selecteddata.getItemAtPosition(listcount.get(i));
//                Log.d(TAG, "save: selectitem-> "+list_selecteddata.getItemAtPosition(listcount.get(i)));
//                Log.d(TAG, "save: data-> " + i + " -> " + user.getDataname());
//                listofdata.add(user.getDataname());
//            }
//        }

//        for(Integer i : listcount){
//            Log.d(TAG, "save: sp.valueAt -> "+sp.valueAt(i)+" listcount.get() "+i);
//            if (sp.valueAt(i)){
//                SelectItem user = (SelectItem) list_selecteddata.getItemAtPosition(i);
//                Log.d(TAG, "save: selectitem-> "+list_selecteddata.getItemAtPosition(i));
//                Log.d(TAG, "save: data-> " + i + " -> " + user.getDataname());
//                listofdata.add(user.getDataname());
//            }
//        }
        Log.d(TAG, "save: -->. " + listofdata);
        Intent intent = new Intent();
        intent.putExtra("senddata", listofdata);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}