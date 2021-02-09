package com.example.myapplication.clientsprofile.clients_fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.All_JobPost;
import com.example.myapplication.Adapter.Client_Booking_Adapter;
import com.example.myapplication.Adapter.ModelsBookingAdapter;
import com.example.myapplication.Adapter.PopularModelsAdapter;
import com.example.myapplication.Adapter.RecommendedModelsAdapter;
import com.example.myapplication.BottomSheet_for_error;
import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.TopFemaleModels;
import com.example.myapplication.pojo.Client_booking_detailsPojo;
import com.example.myapplication.pojo.GetModelData;
import com.example.myapplication.pojo.JobPostdetail_pojo;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.pojo.Model_allContract_pojo;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bookings extends Fragment {

    private static final String TAG = "BookingsFragment";
    private ShimmerRecyclerView rv_modelBookings;
    private ModelsBookingAdapter modelsBookingAdapter;
    private ArrayList<Model_allContract_pojo> modelBookings;
    private ArrayList<Client_booking_detailsPojo> client_booking_detailsPojos;
    private Client_Booking_Adapter client_booking_adapter;
    private StringRequest fetch_bookingsWithModel;
    private SwipeRefreshLayout referesh_option;
    private LinearLayout nocontractsfoundlayout;
    private ImageView menuicon;
    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener;
    private PowerMenu powerMenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clients_contracts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_integration(view);
//            addingData();
        vollyerccall_forBooking_list();
    }
//    private void addingData() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Model_allContract_pojo getModelData = new Model_allContract_pojo();
//                getModelData.setName("hello");
//                getModelData.setJob_title("Android Developer");
//                Model_allContract_pojo getModelData1 = new Model_allContract_pojo();
//                getModelData1.setName("hello");
//                getModelData1.setJob_title("Android Developer");
//                modelBookings.add(getModelData);
//                modelBookings.add(getModelData1);
//                modelsBookingAdapter.notifyDataSetChanged();
//                rv_modelBookings.hideShimmer();
//
//            }
//        },2000);
//        rv_modelBookings.showShimmer();
//    }

    private void rv_integration(View view) {
//        modelBookings = new ArrayList<>();
//        client_booking_detailsPojos = new ArrayList<>();
        // Models Booking
        rv_modelBookings = view.findViewById(R.id.rv_allcontracts_booking);
        nocontractsfoundlayout = view.findViewById(R.id.nocontractsfoundlayout);
        referesh_option = view.findViewById(R.id.referesh_option);
        menuicon = view.findViewById(R.id.menuicon);
        referesh_option.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vollyerccall_forBooking_list();
            }
        });


        onMenuItemClickListener = (position, item) -> {
//            Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            vollyerccall_forBooking_list();
            powerMenu.setSelectedPosition(position); // change selected item
            powerMenu.dismiss();
        };
        powerMenu = new PowerMenu.Builder(Objects.requireNonNull(getContext()))
                .addItem(new PowerMenuItem("Refresh", false)) // add an item.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(getContext(), R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                .setSelectedTextColor(ContextCompat.getColor(getContext(), R.color.black))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();

        menuicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                powerMenu.showAsDropDown(view);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fetch_bookingsWithModel != null) {
            fetch_bookingsWithModel.cancel();
        }
    }

    /**
     * Api call for getting the list of bookings with models
     */
    public void vollyerccall_forBooking_list() {
        modelBookings = new ArrayList<>();
        client_booking_detailsPojos = new ArrayList<>();

        rv_modelBookings.showShimmer();
        Emptylayout();
        Log.d(TAG, "vollyerccall_forjob_list: *****8");
        fetch_bookingsWithModel = new StringRequest(Request.Method.GET,
                ApiConstant.CLIENT_BOOKING_HIRE_TALENT,
                (Response.Listener<String>) response -> {
                    Log.e(TAG, "vollyerccall_forjob_list: response-> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            responsefetch(jsonObject);
                        } else {
                            Log.d(TAG, "vollyerccall_forjob_list: " + jsonObject.getString(ConstantString.RESPONSE_MESSAGE));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, "initCallApi: error-> " + error.toString());
                    showvaldationError(error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> login_token = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getActivity()).getUserData();
                Log.d(TAG, "getHeaders: for  value-> " + logindata.getToken());
                login_token.put("Authorization", "Bearer " + logindata.getToken());
                return login_token;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(fetch_bookingsWithModel);
    }

    /**
     * @param jsonObject jsonObject is having all the information of the top Female models
     */
    private void responsefetch(JSONObject jsonObject) {

        try {
//            Log.d(TAG, "responsefetch: jsonobject-> "+ jsonObject.getJSONObject(ConstantString.DETAIL_TAG));
            JSONArray detailobjec = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);
            Log.d(TAG, "responsefetch: jsonobject-> " + detailobjec + " detailobjectlength-> " + detailobjec.length());
            for (int i = 0; i < detailobjec.length(); i++) {
                JSONObject jobpost = (JSONObject) detailobjec.get(i);


                JSONArray skillarray = jobpost.getJSONArray("skill");
                Log.d(TAG, "responsefetch: skillarray-> " + skillarray);
                ArrayList<String> skilla = new ArrayList<>();
                for (int p = 0; p < skillarray.length(); p++) {
                    skilla.add(skillarray.get(p).toString());
                }
                JSONArray additionattach = jobpost.getJSONArray("additional_attachment");
                ArrayList<String> additionattacharray = new ArrayList<>();
                for (int p = 0; p < additionattach.length(); p++) {
                    additionattacharray.add(additionattach.get(p).toString());
                }

                JSONArray hair_colorjson = jobpost.getJSONArray("hair_color");
                ArrayList<String> hair_colorarray = new ArrayList<>();
                for (int p = 0; p < hair_colorjson.length(); p++) {
                    hair_colorarray.add(hair_colorjson.get(p).toString());
                }
                JSONArray eye_colorjson = jobpost.getJSONArray("eye_color");
                ArrayList<String> eye_colorarray = new ArrayList<>();
                for (int p = 0; p < eye_colorjson.length(); p++) {
                    eye_colorarray.add(eye_colorjson.get(p).toString());
                }
                JSONArray hair_lengthjson = jobpost.getJSONArray("hair_length");
                ArrayList<String> hair_lengtharray = new ArrayList<>();
                for (int p = 0; p < hair_lengthjson.length(); p++) {
                    hair_lengtharray.add(hair_lengthjson.get(p).toString());
                }
                JSONArray skiethnicitylljson = jobpost.getJSONArray("ethnicity");
                ArrayList<String> skiethnicityllarray = new ArrayList<>();
                for (int p = 0; p < skiethnicitylljson.length(); p++) {
                    skiethnicityllarray.add(skiethnicitylljson.get(p).toString());
                }
                JSONArray jobtypejson = jobpost.getJSONArray("job_type");
                Log.e(TAG, "responsefetch: jobtypejson-> "+jobtypejson);
                ArrayList<String> jobtypearray = new ArrayList<>();
                for (int p = 0; p < jobtypejson.length(); p++) {
                    jobtypearray.add(jobtypejson.get(p).toString());
                }



                Log.d(TAG, "\n responsefetch: jsonobject-> post " + jobpost);

                Client_booking_detailsPojo client_booking_detailsPojo = new Client_booking_detailsPojo();
                client_booking_detailsPojo.setContractId(jobpost.getString("contract_id"));
                client_booking_detailsPojo.setStatus(jobpost.getString("status"));
                client_booking_detailsPojo.setId(jobpost.getString("id"));
                client_booking_detailsPojo.setUserId(jobpost.getString("user_id"));
//                client_booking_detailsPojo.setJobSaved(jobpost.getString("job_saved"));
                client_booking_detailsPojo.setRoleAgeMin(jobpost.getString("role_age_min"));
                client_booking_detailsPojo.setRoleAgeMax(jobpost.getString("role_age_max"));
                client_booking_detailsPojo.setHeightFeet(jobpost.getString("height_feet"));
                client_booking_detailsPojo.setHeightInches(jobpost.getString("height_inches"));
                client_booking_detailsPojo.setWeight(jobpost.getString("weight"));
                client_booking_detailsPojo.setAboutProject(jobpost.getString("about_project"));
                client_booking_detailsPojo.setAboutPersonality(jobpost.getString("about_personality"));
                client_booking_detailsPojo.setJobRatePerDay(jobpost.getString("job_rate_per_day"));
                client_booking_detailsPojo.setSkills(skilla);
                client_booking_detailsPojo.setEthnicity(skiethnicityllarray);
                client_booking_detailsPojo.setHairColor(hair_colorarray);

                client_booking_detailsPojo.setEyeColor(eye_colorarray);
                client_booking_detailsPojo.setHairLength(hair_lengtharray);
                client_booking_detailsPojo.setJobTitle(jobpost.getString("job_title"));


                Log.e(TAG, "responsefetch: jobtypearray "+jobtypearray);


                client_booking_detailsPojo.setJobType(jobtypearray);
                client_booking_detailsPojo.setTotalRoles(jobpost.getString("total_roles"));
                client_booking_detailsPojo.setGenderType(jobpost.getString("gender_type"));
                client_booking_detailsPojo.setProductName(jobpost.getString("product_name"));
                client_booking_detailsPojo.setJobDuration(jobpost.getString("job_duration"));
                client_booking_detailsPojo.setRoleDescription(jobpost.getString("role_description"));
                client_booking_detailsPojo.setPerformanceLocation(jobpost.getString("performance_location"));
                client_booking_detailsPojo.setPerformanceFromDate(jobpost.getString("performance_from_date"));
                client_booking_detailsPojo.setPerformanceToDate(jobpost.getString("performance_to_date"));
                client_booking_detailsPojo.setClientInformation(jobpost.getString("client_information"));
                client_booking_detailsPojo.setAdditionalAttachment(additionattacharray);
                client_booking_detailsPojo.setDraft(jobpost.getString("draft"));
                client_booking_detailsPojo.setCreatedAt(jobpost.getString("created_at"));
                client_booking_detailsPojo.setUpdatedAt(jobpost.getString("updated_at"));

                client_booking_detailsPojo.setName(jobpost.getString("name"));

                client_booking_detailsPojo.setTalentId(jobpost.getString("talent_id"));
                client_booking_detailsPojo.setProfile_img(jobpost.getString("profile_img"));
                client_booking_detailsPojo.setFinal_price(jobpost.getString("final_price"));


                client_booking_detailsPojos.add(client_booking_detailsPojo);
                Log.d(TAG, "responsefetch: end loop");

            }
            if (referesh_option != null) {
                referesh_option.setRefreshing(false);
            }
            if (client_booking_detailsPojos.size() == 0) {
                NoEmptylayout();
            } else {
                Emptylayout();
            }


            Log.d(TAG, "responsefetch: client_booking_detailsPojos-> " + client_booking_detailsPojos);
            rv_modelBookings.setLayoutManager(new LinearLayoutManager(getActivity()));
            client_booking_adapter = new Client_Booking_Adapter(client_booking_detailsPojos, getActivity());
            rv_modelBookings.setAdapter(client_booking_adapter);
            rv_modelBookings.setHasFixedSize(true);

            rv_modelBookings.hideShimmer();
            client_booking_adapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void Emptylayout() {
        nocontractsfoundlayout.setVisibility(View.GONE);
    }

    private void NoEmptylayout() {
        nocontractsfoundlayout.setVisibility(View.VISIBLE);
        referesh_option.setVisibility(View.GONE);
    }


    private void showvaldationError(String msg) {
        BottomSheet_for_error bottomSheet_for_error = new BottomSheet_for_error(msg);
        bottomSheet_for_error.setCancelable(false);
        bottomSheet_for_error.show(getActivity().getSupportFragmentManager(), "error bottom");
    }

}
