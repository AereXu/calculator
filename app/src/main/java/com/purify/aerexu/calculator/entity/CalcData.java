package com.purify.aerexu.calculator.entity;

import java.math.BigDecimal;

/**
 * Created by AereXu on 2015/8/23.
 */
public class CalcData {

    public static final int OPERA_CNT_NONE=0;
    public static final int OPERA_CNT_ONCE=OPERA_CNT_NONE+1;
    public static final int OPERA_CNT_MULTI=OPERA_CNT_ONCE+1;
    private Long inNumber;
    private String integerStr;
    private String decimalStr;

    private BigDecimal intermediateOne;
    private BigDecimal intermediateTwo;
    private BigDecimal resultValue;
    private boolean dotStatus;
    private Integer operaCounts;
    private CalcOperaTypes calcOperaTypes;

    public CalcData() {
        intermediateOne = new BigDecimal("0");
        intermediateTwo = new BigDecimal("0");
        resultValue = new BigDecimal("0");
        dotStatus = false;
        operaCounts = OPERA_CNT_NONE;
        integerStr = "0";
        decimalStr = "";
    }

    public void setInNumber(long in) {
        inNumber = in;
    }

    public Long getInNumber() {
        return inNumber;
    }

    public void setIntegerStr(String str) {
        integerStr = str;
    }

    public String getIntegerStr() {
        return integerStr;
    }

    public void setDecimalStr(String in) {
        decimalStr = in;
    }

    public String getDecimalStr() {
        return decimalStr;
    }


    public void setIntermediateOne(BigDecimal in) {
        intermediateOne = in;
    }

    public BigDecimal getIntermediateOne() {
        return intermediateOne;
    }

    public void setIntermediateTwo(BigDecimal in) {
        intermediateTwo = in;
    }

    public BigDecimal getIntermediateTwo() {
        return intermediateTwo;
    }

    public void setResultValue(BigDecimal in) {
        resultValue = in;
    }

    public BigDecimal getResultValue() {
        return resultValue;
    }

    public void setDotStatus(boolean status) {
        dotStatus = status;
    }

    public boolean getDotStatus() {
        return dotStatus;
    }

    public CalcData setOperaCounts(Integer integer) {
        operaCounts = integer;
        return this;
    }

    public Integer getOperaCounts() {
        return operaCounts;
    }

    public CalcData setCalcOperaTypes(CalcOperaTypes operas) {
        calcOperaTypes = operas;
        return this;
    }

    public CalcOperaTypes getCalcOperaTypes() {
        return calcOperaTypes;
    }
}
