package com.example.myapplication.modelsprofile.models_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.clientsprofile.clients_activity.Clients_SelectCriteriaExploreModels;
import com.example.myapplication.modelsprofile.models_activity.Model_Select_Criteria;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.Objects;

public class Model_Browse extends Fragment {
    private LinearLayout titlebar;
    private TextView title;
    private TextView selectcriteria;
    private ShimmerRecyclerView rv_explore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clients_exploremodelsfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
    }
    private void initview(View view){
        titlebar = view.findViewById(R.id.titlebar);
        title = view.findViewById(R.id.title);
        selectcriteria = view.findViewById(R.id.selectcriteria);
        rv_explore = view.findViewById(R.id.rv);
        selectcriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Clients_SelectCriteriaExploreModels.class);
                startActivityForResult(intent,1234);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }
}
