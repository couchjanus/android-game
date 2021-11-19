package com.example.games;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ReStartFragment extends Fragment {

    public ReStartFragment() {
        // Required empty public constructor
        super(R.layout.fragment_re_start);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button restart = view.findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ((GameActivity) getActivity()).run();
            }
        });

    }

}