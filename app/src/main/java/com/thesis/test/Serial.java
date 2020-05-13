package com.thesis.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Serial extends Fragment {

    private View view;
    public Serial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_serial, container, false);

         init();
         return view;
    }

    private void init() {
        TextView t = view.findViewById(R.id.textView);
        t.setText("dfdfdf");

    }
}
