package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.Country_rv_Adapter;
import com.example.myapplication.clientsprofile.Interface.SingleJobOnclick;
import com.example.myapplication.pojo.locationpojo;

import org.jivesoftware.smackx.xdatalayout.packet.DataLayout;

import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Location_Avtivity extends AppCompatActivity implements SingleJobOnclick {
    private ArrayList<locationpojo> location_list;
    private SearchView sv_country ;
    private RecyclerView rv_country;
    private int csc;
    private String title_name;
    private Country_rv_Adapter crv_adapter;
    private TextView title;
    private RelativeLayout empty_language_layout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.location_view_layout);
        location_list = getIntent().getParcelableArrayListExtra("location");
        title_name = getIntent().getStringExtra("title_name");

        csc = getIntent().getIntExtra("csc",0);
        intview();
        setview();
    }
    private void intview(){

        rv_country = findViewById(R.id.rv_country);
        sv_country = findViewById(R.id.sv_country);
        sv_country.setIconified(false);
        sv_country.onActionViewExpanded();
        title = findViewById(R.id.title);
        sv_country.clearFocus();
        sv_country.setImeOptions(EditorInfo.IME_ACTION_DONE);

        EditText searchEditText = (EditText) sv_country.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.text_color_dark_grey));
        searchEditText.setHintTextColor(getResources().getColor(R.color.grey_transparent_50));
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/librereg.ttf");
        searchEditText.setTypeface(tf);
        empty_language_layout = findViewById(R.id.empty_language_layout);




    }
    private void setview(){
         crv_adapter = new Country_rv_Adapter(location_list,this);
        crv_adapter.Setoncountrylistener(this::OnlistSingleListener);
        rv_country.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rv_country.setLayoutManager(new LinearLayoutManager(this));
        rv_country.setAdapter(crv_adapter);
        title.setText(title_name);

        sv_country.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                crv_adapter.getFilter().filter(newText);
                return false;
            }
        });

        if(location_list.isEmpty()){
            rv_country.setVisibility(View.GONE);
            empty_language_layout.setVisibility(View.VISIBLE);
        }
        else{
            rv_country.setVisibility(View.VISIBLE);
            empty_language_layout.setVisibility(View.GONE);
        }

    }

    @Override
    public void OnlistSingleListener(int i) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("singlecountry",location_list.get(i));
        returnIntent.putExtra("csc",csc);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu,menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                crv_adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }
}
