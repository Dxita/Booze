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
public class PendingsellerFragment extends Fragment {

    public PendingsellerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_pendingseller, container, false);

        return v;
    }
}
