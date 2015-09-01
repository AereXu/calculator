package com.purify.aerexu.calculator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;


/**
 * A placeholder fragment containing a simple view.
 */
public class CalcFragButtons extends Fragment implements Button.OnClickListener{
    private Map<Integer, Button> buttonMap = new HashMap<>();
    private View view;
    public CalcFragButtons() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calc_buttons, container, false);
        initButtonMap();
        return view;
    }

    private void initButtonMap() {
        Integer[] butIds = {
                R.id.mButDel, R.id.mButCle, R.id.mButClr, R.id.mButSgn, R.id.mButSqr,
                R.id.mButOne, R.id.mButTwo, R.id.mButThr, R.id.mButDiv, R.id.mButPer,
                R.id.mButFou, R.id.mButFiv, R.id.mButSix, R.id.mButMul, R.id.mButRec,
                R.id.mButSev, R.id.mButEig, R.id.mButNin, R.id.mButSub, R.id.mButEql,
                R.id.mButZer, R.id.mButDot, R.id.mButAdd
        };
        for (Integer butId : butIds) {
            view.findViewById(butId).setOnClickListener(this);
            buttonMap.put(butId, (Button) view.findViewById(butId));
        }
    }
    @Override
    public void onClick(View v) {
//        CalcProcessor.getInstance().process(v.getId());
//        CalcFactoryImpl CFI = new CalcFactoryImpl();
//        CFI.getCalc().process(v.getId());
        CalcProcess calcImpl=new CalcFactoryImpl().getCalc();
        calcImpl.process(v.getId());
        mCallback.onButtonClicked(calcImpl.getCalcResult());
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    OnButtonClickedListener mCallback;
    // Container Activity must implement this interface
    public interface OnButtonClickedListener {
        public void onButtonClicked(String calcResult);
    }
}