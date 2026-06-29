package com.mohitingale.agroguide.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mohitingale.agroguide.R;
public class MyFarmFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_farm_, container, false);

        Toast.makeText(getActivity(), "My Farm Fragment Open", Toast.LENGTH_SHORT).show();

        return view;
    }
}