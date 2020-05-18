package com.sky21.liquor_app.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky21.liquor_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendinguserFragment extends Fragment {

    public PendinguserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pendinguser, container, false);
    }
}
