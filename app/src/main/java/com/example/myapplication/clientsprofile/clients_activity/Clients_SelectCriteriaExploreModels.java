package com.example.myapplication.clientsprofile.clients_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public class Clients_SelectCriteriaExploreModels extends AppCompatActivity {
   public String resulttxt = "resulttxt";

   public String searchApi = "";
   private static  final String TAG = " Client_selectExplor";

    private TextView title;
    private ImageView dropdowngender;
    private LinearLayout genderll;
    private CheckBox male;
    private CheckBox female;
    private CheckBox both;

    private ImageView dropdownskills;
    private RadioGroup skills;
    private RadioButton malemodel;
    private RadioButton femalemodel;
    private RadioButton photography;
    private RadioButton makeupartists;
    private RadioButton hairstylist;
    private RadioButton wardrobepartner;
    private RadioButton closthingdesigner;
    private RadioButton bikes;
    private RadioButton cars;
    private RadioButton stunts;
    private RadioButton generalsports;
    private RadioButton Acting;
    private RadioButton Swimming;
    private RadioButton Singing;
    private RadioButton Dancing;
    private RadioButton Comedy;
    private RadioButton Gymnast;
    private RadioButton Action;

    private ImageView dropdownethic;
    private RadioGroup ethnicrg;
    private RadioButton Indian;
    private RadioButton Asian;
    private RadioButton Southeast;
    private RadioButton Middle;
    private RadioButton African;
    private RadioButton Caucasian;
    private RadioButton Hispanic;
    private RadioButton Mixed;
    private RadioButton Africandescant;

    private ImageView dropdownbody;
    private RadioGroup bodyrg;
    private RadioButton Average;
    private RadioButton Slim;
    private RadioButton Athletic;
    private RadioButton Muscular;
    private RadioButton Plus;
    private RadioButton Curvy;
    private RadioButton Tall;
    private RadioButton Short;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clients_selectcriteriaformodelsexplore);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        Log.d(TAG, "onCreate: ");
        initview();




    }

    private void initview(){
        dropdowngender = findViewById(R.id.dropdowngender);
        genderll = findViewById(R.id.genderll);

        dropdownskills = findViewById(R.id.dropdownskills);
        skills =findViewById(R.id.skills);

        dropdownethic = findViewById(R.id.dropdownethic);
        ethnicrg = findViewById(R.id.ethnicrg);

        dropdownbody = findViewById(R.id.dropdownbody);
        bodyrg = findViewById(R.id.bodyrg);
        initviewgender();
    }


    private void initviewgender(){
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        both = findViewById(R.id.both);
        setonclickgender();

    }
    private void initviewskill(){

        malemodel = findViewById(R.id.malemodel);
        femalemodel = findViewById(R.id.femalemodel);
        photography = findViewById(R.id.photography);
        makeupartists = findViewById(R.id.makeupartists);
        hairstylist = findViewById(R.id.hairstylist);
        wardrobepartner = findViewById(R.id.wardrobepartner);
        closthingdesigner = findViewById(R.id.closthingdesigner);
        bikes = findViewById(R.id.bikes);
        cars = findViewById(R.id.cars);
        stunts = findViewById(R.id.stunts);
        generalsports = findViewById(R.id.generalsports);
        Acting = findViewById(R.id.Acting);
        Swimming = findViewById(R.id.Swimming);
        Singing = findViewById(R.id.Singing);
        Dancing = findViewById(R.id.Dancing);
        Comedy = findViewById(R.id.Comedy);
        Gymnast = findViewById(R.id.Gymnast);
        Action = findViewById(R.id.Action);

    }
    private void initviewEthinc(){
        Indian = findViewById(R.id.Indian);
        Asian = findViewById(R.id.Asian);
        Southeast = findViewById(R.id.Southeast);
        Middle = findViewById(R.id.Middle);
        African = findViewById(R.id.African);
        Caucasian = findViewById(R.id.Caucasian);
        Hispanic = findViewById(R.id.Hispanic);
        Mixed = findViewById(R.id.Mixed);
        Africandescant = findViewById(R.id.Africandescant);
    }
    private void initviewBody(){
        Average = findViewById(R.id.Average);
        Slim = findViewById(R.id.Slim);
        Athletic = findViewById(R.id.Athletic);
        Muscular = findViewById(R.id.Muscular);
        Plus = findViewById(R.id.Plus);
        Curvy = findViewById(R.id.Curvy);
        Tall = findViewById(R.id.Tall);
        Short = findViewById(R.id.Short);
    }

    public void gendermain(View view) {
        if(genderll.getVisibility() == View.VISIBLE){
            dropdowngender.setRotation(0);
            genderll.setVisibility(View.GONE);
        }
        else{
            dropdowngender.setRotation(90);
            genderll.setVisibility(View.VISIBLE);
        }

        dropdownskills.setRotation(0);
        dropdownethic.setRotation(0);
        dropdownbody.setRotation(0);

        skills.setVisibility(View.GONE);
        ethnicrg.setVisibility(View.GONE);
        bodyrg.setVisibility(View.GONE);
//        initviewgender();

//        setonclickgender();


    }


    private void setonclickgender(){

        searchApi = ApiConstant.CLIENT_EXPLORE_BY_GENDER;
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged: male "+buttonView.isChecked());
                if(buttonView.isChecked()){

                    sendintent("male",searchApi);
                }

            }
        });
        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged: male "+buttonView.isChecked());
                if(buttonView.isChecked()){
                    sendintent("female",searchApi);
                }

            }
        });

        both.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged: male "+buttonView.isChecked());
                if(buttonView.isChecked()){
                    sendintent("Both",searchApi);
                }

            }
        });





    }



    public void specialskillsmain(View view){
        Log.d(TAG, "specialskillsmain: clicked- ");
        if(skills.getVisibility() == View.VISIBLE){
            dropdownskills.setRotation(0);
            skills.setVisibility(View.GONE);
        }
        else{
            dropdownskills.setRotation(90);
            skills.setVisibility(View.VISIBLE);
        }

        dropdowngender.setRotation(0);
        dropdownethic.setRotation(0);
        dropdownbody.setRotation(0);

        genderll.setVisibility(View.GONE);
        ethnicrg.setVisibility(View.GONE);
        bodyrg.setVisibility(View.GONE);

        initviewskill();
        skills.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "onCheckedChanged: checkedid-> "+checkedId);
                setonclickskill(checkedId);
            }
        });
//        setonclickskill();
    }

    private void setonclickskill(int checkedId){
        searchApi = ApiConstant.CLIENT_EXPLORE_BY_SKILL;
        Log.d(TAG, "setonclickskill: checkedID-> "+checkedId+" R.id.malemodel "+(R.id.malemodel)+
                " searchApi->  "+searchApi);
        if(checkedId == R.id.malemodel){
            Log.d(TAG, "setonclickskill: serachApi-> "+searchApi);
            sendintent("Male Model",searchApi);
        }
        else if(checkedId == R.id.femalemodel){
            sendintent("Female Model",searchApi);
        }
        else if(checkedId == R.id.photography){
            sendintent("Photography",searchApi);
        }
        else if(checkedId == R.id.makeupartists){
            sendintent("Makeup Artist",searchApi);
        } else if(checkedId == R.id.hairstylist){
            sendintent("Hair Stylist",searchApi);
        } else if(checkedId == R.id.wardrobepartner){
            sendintent("Wardrobe Partner",searchApi);
        } else if(checkedId == R.id.closthingdesigner){
            sendintent("Clothing designer",searchApi);
        }
        else if(checkedId == R.id.bikes){
            sendintent("bikes",searchApi);
        }
        else if(checkedId == R.id.cars){
            sendintent("Cars",searchApi);
        }
        else if(checkedId == R.id.stunts){
            sendintent("Stunts",searchApi);
        }
        else if(checkedId == R.id.generalsports){
            sendintent("General Sports",searchApi);
        }else if(checkedId == R.id.Acting){
            sendintent("Acting",searchApi);
        } else if(checkedId == R.id.Swimming){
            sendintent("Swimming",searchApi);
        } else if(checkedId == R.id.Singing){
            sendintent("Singing",searchApi);
        }
        else if(checkedId == R.id.Dancing){
            sendintent("Dancing",searchApi);
        }
        else if(checkedId == R.id.Comedy){
            sendintent("Comedy",searchApi);
        }
        else if(checkedId == R.id.Gymnast){
            sendintent("Gymnast",searchApi);
        }
        else if(checkedId == R.id.Action){
            sendintent("Action",searchApi);
        }

    }


    public void ethnicmain(View view){

        if(ethnicrg.getVisibility() == View.VISIBLE){
            dropdownethic.setRotation(0);
            ethnicrg.setVisibility(View.GONE);
        }
        else{
            dropdownethic.setRotation(90);
            ethnicrg.setVisibility(View.VISIBLE);
        }

        dropdowngender.setRotation(0);
        dropdownskills.setRotation(0);
        dropdownbody.setRotation(0);


        genderll.setVisibility(View.GONE);
        skills.setVisibility(View.GONE);
        bodyrg.setVisibility(View.GONE);

        initviewEthinc();
        ethnicrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setonclickethic(checkedId);
            }
        });

    }

    private void setonclickethic(int checkedId){
        searchApi = ApiConstant.CLIENT_EXPLORE_BY_EHIN;
        if(checkedId == R.id.Indian){
            sendintent("Indian",searchApi);
        }
        else if(checkedId == R.id.Asian){
            sendintent("Asian",searchApi);
        }
        else if(checkedId == R.id.Southeast){
            sendintent("Southeast asian",searchApi);
        }
        else if(checkedId == R.id.Middle){
            sendintent("Middle eastern",searchApi);

        }
        else if(checkedId == R.id.African){
            sendintent("African American",searchApi);
        }
        else if(checkedId == R.id.Caucasian){
            sendintent("Caucasian",searchApi);
        } else if(checkedId == R.id.Hispanic){
            sendintent("Hispanic",searchApi);
        }
        else if(checkedId == R.id.Mixed){
            sendintent("Mixed race",searchApi);
        }
        else if(checkedId == R.id.Africandescant){
            sendintent("African descent",searchApi);
        }


    }


    public void bodymain(View view){
        if(bodyrg.getVisibility() == View.VISIBLE){
            dropdownbody.setRotation(0);
            bodyrg.setVisibility(View.GONE);
        }
        else{
            dropdownbody.setRotation(90);
            bodyrg.setVisibility(View.VISIBLE);
        }

        dropdowngender.setRotation(0);
        dropdownskills.setRotation(0);
        dropdownethic.setRotation(0);


        genderll.setVisibility(View.GONE);
        skills.setVisibility(View.GONE);
        ethnicrg.setVisibility(View.GONE);

        initviewBody();
        bodyrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setonclickbodytypec(checkedId);
            }
        });

    }

    private void setonclickbodytypec(int checkedId){
        searchApi = ApiConstant.CLIENT_EXPLORE_BY_BODYTYPE;

        if(checkedId == R.id.Average){
            sendintent("Average",searchApi);
        }
        else if(checkedId == R.id.Slim){
            sendintent("Slim",searchApi);
        }
        else if(checkedId == R.id.Athletic){
            sendintent("Athletic",searchApi);
        }
        else if(checkedId == R.id.Muscular){
            sendintent("Muscular",searchApi);
        } else if(checkedId == R.id.Plus){
            sendintent("Plus size",searchApi);
        } else if(checkedId == R.id.Curvy){
            sendintent("Curvy",searchApi);
        } else if(checkedId == R.id.Tall){
            sendintent("Tall",searchApi);
        }
        else if(checkedId == R.id.Short){
            sendintent("Short",searchApi);
        }

    }



    private void sendintent(String resultsend,String searchApi){
        if(!resultsend.isEmpty()){
            Intent intent = new Intent();
            intent.putExtra(resulttxt,resultsend);
            intent.putExtra("searchApi",searchApi);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }
}
