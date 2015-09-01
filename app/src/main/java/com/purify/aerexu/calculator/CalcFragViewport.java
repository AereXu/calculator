package com.purify.aerexu.calculator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CalcFragViewport extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calc_viewport, container, false);
    }
    public void updateCalcResult(String calcResult){
        TextView calcResultView = (TextView)getActivity().findViewById(R.id.calcViewportTextView);
        calcResultView.setText(calcResult);
    }
}
