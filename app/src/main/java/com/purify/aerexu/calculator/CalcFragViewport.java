package com.purify.aerexu.calculator;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.purify.aerexu.calculator.entity.CalcStrings;


public class CalcFragViewport extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calc_viewport, container, false);
    }
    public void updateCalcResult(CalcStrings calcStrings){
        TextView calcTmpView = (TextView)getActivity().findViewById(R.id.calcViewportResultTV);
        calcTmpView.setText(calcStrings.getResultStr());
        calcTmpView = (TextView)getActivity().findViewById(R.id.calcViewportIntermediateOneTV);
        calcTmpView.setText(calcStrings.getIntermediateOneStr());
        calcTmpView = (TextView)getActivity().findViewById(R.id.calcViewportIntermediateTwoTV);
        calcTmpView.setText(calcStrings.getIntermediateTwoStr());
        calcTmpView = (TextView)getActivity().findViewById(R.id.calcViewportOperatorTV);
        calcTmpView.setText(calcStrings.getCalcOperatorStr());
    }



}
