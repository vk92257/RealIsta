package com.example.myapplication.clientsprofile.clients_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.Adapter.AllProposals_Adapter;
import com.example.myapplication.R;
import com.example.myapplication.modelsprofile.models_activity.Models_submitProposal;
import com.example.myapplication.pojo.AllProposal_Pojo;
import com.example.myapplication.pojo.LoginTimesaveData;
import com.example.myapplication.utils.ApiConstant;
import com.example.myapplication.utils.ConstantString;
import com.example.myapplication.utils.SharedPreferanceManager;
import com.example.myapplication.utils.VolleySingleton;
import com.todkars.shimmer.ShimmerRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class All_Job_Proposals_Fragment extends Fragment {
    private ShimmerRecyclerView proposal_Rv;
    private LinearLayout noproposalsfoundlayout;
    private StringRequest proposalrequest;
    private String JobDetail_jobid;
    private ArrayList<AllProposal_Pojo> pojoAll_proposal;
    private AllProposal_Pojo proposal_pojo;
    public static final String TAG = "All_job_Proposals_Fragment";

    public All_Job_Proposals_Fragment(String jobDetail_jobid) {
        JobDetail_jobid = jobDetail_jobid;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("papa alljobpost", "onCreateView: ");
        return inflater.inflate(R.layout.clients_viewallproposals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("papa alljobpost", "onViewCreated: ");
        initview(view);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("papa alljobpost", "onStart: ");
        volleyapicall();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("papa alljobpost", "onResume: ");
    }

    @Override
    public void onStop() {
        Log.d("papa alljobpost", "onStop: ");
        super.onStop();
        if (proposalrequest != null) {
            proposalrequest.cancel();
        }

    }


    private void initview(View view) {

        noproposalsfoundlayout = view.findViewById(R.id.noproposalsfoundlayout);
        proposal_Rv = view.findViewById(R.id.rv);
    }

    private void volleyapicall() {
        Log.d("papa alljobpost", "volleyapicall: ");
        pojoAll_proposal = new ArrayList<>();
        proposalrequest = new StringRequest(Request.Method.POST,
                ApiConstant.ALL_PROPOSALS,
                (Response.Listener<String>) response -> {
                    Log.d(TAG, "volleyapicall: response-> " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean(ConstantString.IS_ERROR)) {
                            JSONArray detailobjec = jsonObject.getJSONArray(ConstantString.DETAIL_TAG);
                            for (int i = 0; i < detailobjec.length(); i++) {
                                JSONObject job_proposal = (JSONObject) detailobjec.get(i);

                                proposal_pojo = new AllProposal_Pojo();
//                                proposal_pojo = new AllProposal_Pojo(
//                                        job_proposal.getString("id"),
//                                        job_proposal.getString("job_id"),
//                                        job_proposal.getString("user_id"),
//                                        job_proposal.getString("purposal_rate"),
//                                        job_proposal.getString("attachment"),
//                                        job_proposal.getString("invitation"),
//                                        job_proposal.getString("created_at"),
//                                        job_proposal.getString("updated_at"),
//                                        job_proposal.getString("name"),
//                                        job_proposal.getString("city"),
//                                        job_proposal.getString("state"),
//                                        job_proposal.getString("country"),
//                                        job_proposal.getString("skills"),
//                                        job_proposal.getString("proffesion"),
//                                        job_proposal.getString("profile_img"),
//                                        job_proposal.getString("cover_letter")
//                                );
                                JSONArray attachment_json = job_proposal.getJSONArray("attachment");
                                ArrayList<String> attachmentarray = new ArrayList<>();
                                for(int p=0;p<attachment_json.length();p++){
                                    attachmentarray.add(attachment_json.get(p).toString());
                                }
                                JSONArray skillarray = job_proposal.getJSONArray("skill");
                                Log.d(TAG, "responsefetch: skillarray-> "+skillarray);
                                ArrayList<String> skilla = new ArrayList<>();
                                for(int p=0;p<skillarray.length();p++){
                                    skilla.add(skillarray.get(p).toString());
                                }

                                proposal_pojo.setId( job_proposal.getString("id"));
                                proposal_pojo.setUserId(job_proposal.getString("user_id"));
                                proposal_pojo.setJobId( job_proposal.getString("job_id"));
                                proposal_pojo.setName(job_proposal.getString("name"));
                                proposal_pojo.setPurposalRate(job_proposal.getString("purposalRate"));
                                proposal_pojo.setAttachment( attachmentarray);
                                proposal_pojo.setInvitation(job_proposal.getString("invitation"));
                                proposal_pojo.setCreatedAt(job_proposal.getString("created_at"));
                                proposal_pojo.setUpdatedAt( job_proposal.getString("updated_at"));


                                proposal_pojo.setCity(job_proposal.getString("cityName"));
                                proposal_pojo.setState(job_proposal.getString("stateName"));
                                proposal_pojo.setCountry(job_proposal.getString("countryName"));
                                proposal_pojo.setSkills(skilla);
                                proposal_pojo.setProffesion(job_proposal.getString("proffesion"));
                                proposal_pojo.setProfile_img(job_proposal.getString("profile_img"));
                                proposal_pojo.setCover_letter(job_proposal.getString("coverLetter"));




                                pojoAll_proposal.add(proposal_pojo);
                                Log.d(TAG, "volleyapicall:pojoall_proposalValue->  "+pojoAll_proposal.get(0).getAttachment());
                            }
                            Log.d(TAG, "volleyapicall: " + pojoAll_proposal);

                            if (!pojoAll_proposal.isEmpty()) {
                                proposal_Rv.hideShimmer();
                                HideEmpty();
                                AllProposals_Adapter allProposals_adapter = new AllProposals_Adapter(pojoAll_proposal, getContext());
                                proposal_Rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                proposal_Rv.setAdapter(allProposals_adapter);

                            } else {
                                ShowEmpty();
                            }
                        } else {
                            proposal_Rv.hideShimmer();
                            Toast.makeText(getContext(), "No Proposals to show ", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {

                    }
                },
                error -> {
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                    showvaldationError(error.toString());
                }) {
            @SuppressLint("LongLogTag")
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> submitproposal = new HashMap<>();
                LoginTimesaveData logindata = SharedPreferanceManager.getInstance(getActivity()).getUserData();
                Log.d(TAG, "getHeaders: token value-> " + logindata.getToken());
                submitproposal.put("Authorization", "Bearer " + logindata.getToken());
                return submitproposal;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> proposalparam = new HashMap<>();
                proposalparam.put("jobId", JobDetail_jobid);

                return proposalparam;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(proposalrequest);
    }

    private void ShowEmpty() {

        proposal_Rv.setVisibility(View.GONE);
        noproposalsfoundlayout.setVisibility(View.VISIBLE);

    }

    private void HideEmpty() {
        proposal_Rv.setVisibility(View.VISIBLE);
        noproposalsfoundlayout.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("papa alljobpost", "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("papa alljobpost", "onDestroy: ");
    }
}
