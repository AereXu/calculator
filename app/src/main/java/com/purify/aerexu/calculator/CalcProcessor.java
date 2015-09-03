package com.purify.aerexu.calculator;

import com.purify.aerexu.calculator.entity.CalcCalculate;
import com.purify.aerexu.calculator.entity.CalcData;
import com.purify.aerexu.calculator.entity.CalcOperaTypes;
import com.purify.aerexu.calculator.entity.CalcStrings;

/**
 * Created by AereXu on 2015/8/22.
 */
public class CalcProcessor implements CalcProcess {
    private CalcData calcData;
    private CalcCalculate calcCalculate;

    private static class LazyHolder {
        private static final CalcProcessor INSTANCE = new CalcProcessor();
    }

    private CalcProcessor() {
        if (calcData == null) {
            calcData = new CalcData();
        }
        if (calcCalculate == null) {
            calcCalculate = new CalcCalculate();
        }
    }

    public static final CalcProcessor getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public void process(int butID) {
        switch (butID) {
            case R.id.mButOne:
                calcData.setCalcOperaTypes(CalcOperaTypes.ONE).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButTwo:
                calcData.setCalcOperaTypes(CalcOperaTypes.TWO).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButThr:
                calcData.setCalcOperaTypes(CalcOperaTypes.THR).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButFou:
                calcData.setCalcOperaTypes(CalcOperaTypes.FOU).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButFiv:
                calcData.setCalcOperaTypes(CalcOperaTypes.FIV).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButSix:
                calcData.setCalcOperaTypes(CalcOperaTypes.SIX).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButSev:
                calcData.setCalcOperaTypes(CalcOperaTypes.SEV).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButEig:
                calcData.setCalcOperaTypes(CalcOperaTypes.EIG).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButNin:
                calcData.setCalcOperaTypes(CalcOperaTypes.NIN).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButZer:
                calcData.setCalcOperaTypes(CalcOperaTypes.ZER).setOperaCounts(CalcData.OPERA_CNT_NONE);
                break;
            case R.id.mButDel:
                calcData.setCalcOperaTypes(CalcOperaTypes.DEL).setOperaCounts(CalcData.OPERA_CNT_ONCE );
                break;
            case R.id.mButCle:
                calcData.setCalcOperaTypes(CalcOperaTypes.CLE).setOperaCounts(CalcData.OPERA_CNT_ONCE);
                break;
            case R.id.mButClr:
                calcData.setCalcOperaTypes(CalcOperaTypes.CLR).setOperaCounts(CalcData.OPERA_CNT_ONCE);
                break;
            case R.id.mButSgn:
                calcData.setCalcOperaTypes(CalcOperaTypes.SGN).setOperaCounts(CalcData.OPERA_CNT_ONCE);
                break;
            case R.id.mButSqr:
                calcData.setCalcOperaTypes(CalcOperaTypes.SQR).setOperaCounts(CalcData.OPERA_CNT_ONCE);
                break;
            case R.id.mButPer:
                calcData.setCalcOperaTypes(CalcOperaTypes.PER).setOperaCounts(CalcData.OPERA_CNT_ONCE);
                break;
            case R.id.mButRec:
                calcData.setCalcOperaTypes(CalcOperaTypes.REC).setOperaCounts(CalcData.OPERA_CNT_ONCE);
                break;
            case R.id.mButDiv:
                calcData.setCalcOperaTypes(CalcOperaTypes.DIV).setOperaCounts(calcData.getOperaCounts()+ CalcData.OPERA_CNT_MULTI);
                break;
            case R.id.mButMul:
                calcData.setCalcOperaTypes(CalcOperaTypes.MUL).setOperaCounts(calcData.getOperaCounts()+ CalcData.OPERA_CNT_MULTI);
                break;
            case R.id.mButAdd:
                calcData.setCalcOperaTypes(CalcOperaTypes.ADD).setOperaCounts(calcData.getOperaCounts()+ CalcData.OPERA_CNT_MULTI);
                break;
            case R.id.mButSub:
                calcData.setCalcOperaTypes(CalcOperaTypes.SUB).setOperaCounts(calcData.getOperaCounts()+ CalcData.OPERA_CNT_MULTI);
                break;
            case R.id.mButEql:
                calcData.setCalcOperaTypes(CalcOperaTypes.EQL).setOperaCounts(calcData.getOperaCounts()+ CalcData.OPERA_CNT_MULTI);
                break;
            case R.id.mButDot:
                calcData.setCalcOperaTypes(CalcOperaTypes.DOT).setOperaCounts(CalcData.OPERA_CNT_ONCE);
                break;
        }
        calcCalculate.refreshCalcData(calcData);
    }

    @Override
    public CalcStrings getCalcResult() {
        CalcStrings calcStrings = new CalcStrings();
        calcStrings.setResultStr(calcCalculate.getCalcShowStr());
        calcStrings.setIntermediateOneStr(calcCalculate.getIntermediateOneStr());
        calcStrings.setIntermediateTwoStr(calcCalculate.getIntermediateTwoStr());
        calcStrings.setCalcOperatorStr(calcCalculate.getCalcOperatorStr());
        return calcStrings;
    }
}
