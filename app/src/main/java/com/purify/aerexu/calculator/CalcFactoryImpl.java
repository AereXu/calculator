package com.purify.aerexu.calculator;

/**
 * Created by AereXu on 2015/8/22.
 */
public class CalcFactoryImpl implements CalcFoctory {
    @Override
    public CalcProcess getCalc(){
        return CalcProcessor.getInstance();
    }
}
