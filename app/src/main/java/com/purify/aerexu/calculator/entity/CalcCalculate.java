package com.purify.aerexu.calculator.entity;

import java.math.BigDecimal;

/**
 * Created by AereXu on 2015/8/23.
 */
public class CalcCalculate {
    public static final int MAX_LENGTH = 32;
    public static final long XX_CARRY = 10;
    private String CalcShowStr;
    private CalcOperaTypes preMathOpera;
    private CalcOperaTypes newMathOpera;
    private CalcOperGroups foreOpera;
    private CalcOperGroups nowOpera;
    private boolean illegalStatus;
    private boolean editbal;

    public CalcCalculate() {
        CalcShowStr = "";
        preMathOpera = CalcOperaTypes.NUL;
        newMathOpera = CalcOperaTypes.NUL;
        illegalStatus = false;
        editbal = false;
    }

    public void refreshCalcData(CalcData calcData) {
        foreOpera = nowOpera;
        nowOpera = CalcOperGroups.calcType2Group(calcData.getCalcOperaTypes());
        if (illegalStatus) {
            switch (calcData.getCalcOperaTypes()) {
                case CLE:
                case CLR:
                    illegalStatus = false;
                    calcResetResult(calcData);
                    calcResetInput(calcData);
                    calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                    CalcShowStr = calcData.getIntermediateOne().toString();
            }
        } else {
            if (calcData.getOperaCounts() == CalcData.OPERA_CNT_ONCE) {//Operation like add, sub, mul, is on

                switch (calcData.getCalcOperaTypes()) {
                    case DOT:
                        calcData.setDotStatus(true);
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        preMathOpera = newMathOpera;
                        if (!calcData.getDecimalStr().equals("")) { // 0.2, 1.2
                            CalcShowStr = calcData.getIntegerStr() + "." + calcData.getDecimalStr();
                        } else {//1., 0.
                            CalcShowStr = calcData.getIntegerStr() + ".";
                        }
                        calcData.setIntermediateOne(new BigDecimal(calcData.getIntegerStr() + "." + calcData.getDecimalStr()));
                        break;
                    case SQR:
                        foreOperaCalcCheck(calcData);
                        if (calcData.getIntermediateOne().compareTo(new BigDecimal("0")) < 0) {
                            illegalStatus = true;
                            CalcShowStr = "Illegal input!";
                            return;
                        }
                        calcData.setIntermediateOne(BigDecimalSqrt(calcData.getIntermediateOne(), MAX_LENGTH));
                        calcResetInput(calcData);
                        CalcShowStr = calcData.getIntermediateOne().toString();
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        break;
                    case PER:
                        foreOperaCalcCheck(calcData);
                        calcData.setIntermediateOne(calcData.getIntermediateTwo().multiply(calcData.getIntermediateOne()).divide(new BigDecimal("100")));
                        calcResetInput(calcData);
                        CalcShowStr = calcData.getIntermediateOne().toString();
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        break;
                    case REC:
                        foreOperaCalcCheck(calcData);
                        try {
//                            String tmp = new BigDecimal("1").divide(calcData.getIntermediateOne(), MAX_LENGTH, BigDecimal.ROUND_CEILING).toPlainString();
//                            calcData.setIntermediateOne(new BigDecimal(deleteExtraZeros(tmp)));
                            calcData.setIntermediateOne(new BigDecimal("1").divide(calcData.getIntermediateOne(), MAX_LENGTH, BigDecimal.ROUND_CEILING).stripTrailingZeros());
                        } catch (ArithmeticException e) {
                            if (calcData.getIntermediateOne().intValue() == 0) {
                                illegalStatus = true;
                                CalcShowStr = "Illegal zero!";
                                return;
                            }
                        }
                        calcResetInput(calcData);
                        CalcShowStr = calcData.getIntermediateOne().toString();
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        break;
                    case SGN:
                        foreOperaCalcCheck(calcData);
                        if (calcData.getIntermediateOne().compareTo(new BigDecimal("0.0")) >= 0) {//positive
                            calcData.setIntermediateOne(new BigDecimal("-" + calcData.getIntermediateOne().toString()));
                        } else {
                            calcData.setIntermediateOne(new BigDecimal(calcData.getIntermediateOne().toString().substring(1)));
                        }
                        calcData.setIntegerStr(String.valueOf(calcData.getIntermediateOne().intValue()));
                        calcData.setDecimalStr(getDecimalStr(calcData.getIntermediateOne().toString()));
                        CalcShowStr = calcData.getIntermediateOne().toString();
                        if (calcData.getDotStatus() && calcData.getDecimalStr().equals("")) {
                            CalcShowStr = CalcShowStr + ".";
                        }
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        break;
                    case DEL:
                        if (editbal) {
                            if (calcData.getDecimalStr().length() > 0) {
                                calcData.setDecimalStr(calcData.getDecimalStr().substring(0, calcData.getDecimalStr().length() - 1));
                                CalcShowStr = calcData.getIntegerStr() + "." + calcData.getDecimalStr();
                            } else {
                                if (calcData.getDotStatus()) {
                                    calcData.setDotStatus(false);

                                } else {
                                    if (calcData.getIntegerStr().charAt(0) != '-' && calcData.getIntegerStr().length() == 1) {
                                        calcData.setIntegerStr("0");
                                    } else if (calcData.getIntegerStr().charAt(0) == '-' && calcData.getIntegerStr().length() == 2) {
                                        calcData.setIntegerStr("0");
                                    } else {
                                        calcData.setIntegerStr(calcData.getIntegerStr().substring(0, calcData.getIntegerStr().length() - 1));
                                    }
                                }
                                CalcShowStr = calcData.getIntegerStr();
                            }
                            calcData.setIntermediateOne(new BigDecimal(CalcShowStr));
                            calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        }
                        break;
                    case CLE:
                        calcResetInput(calcData);
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        CalcShowStr = calcData.getIntermediateOne().toString();
                        break;
                    case CLR:
                        calcResetResult(calcData);
                        calcResetInput(calcData);
                        CalcShowStr = calcData.getIntermediateOne().toString();
                        break;
                }
            } else if (calcData.getOperaCounts() == CalcData.OPERA_CNT_NONE) {
                if (newMathOpera == CalcOperaTypes.EQL) {
                    calcData.setIntegerStr("0");
                    calcData.setDecimalStr("");
                    newMathOpera = CalcOperaTypes.NUL;
                }
                switch (calcData.getCalcOperaTypes()) {
                    case ONE:
                        calcData.setInNumber(1);
                        break;
                    case TWO:
                        calcData.setInNumber(2);
                        break;
                    case THR:
                        calcData.setInNumber(3);
                        break;
                    case FOU:
                        calcData.setInNumber(4);
                        break;
                    case FIV:
                        calcData.setInNumber(5);
                        break;
                    case SIX:
                        calcData.setInNumber(6);
                        break;
                    case SEV:
                        calcData.setInNumber(7);
                        break;
                    case EIG:
                        calcData.setInNumber(8);
                        break;
                    case NIN:
                        calcData.setInNumber(9);
                        break;
                    case ZER:
                        calcData.setInNumber(0);
                        break;
                }
                preMathOpera = newMathOpera;
                editbal = true;
                if (calcData.getDotStatus()) {//Dot is on
                    if (calcData.getIntegerStr().length() + calcData.getDecimalStr().length() <= MAX_LENGTH) {
                        calcData.setDecimalStr(calcData.getDecimalStr() + calcData.getInNumber().toString());
                    }
                    CalcShowStr = calcData.getIntegerStr() + "." + calcData.getDecimalStr();
                } else {
                    if (calcData.getIntegerStr().length() + calcData.getDecimalStr().length() < MAX_LENGTH) {
                        calcData.setIntegerStr(String.valueOf(Long.parseLong(calcData.getIntegerStr()) >= 0 ?
                                Long.parseLong(calcData.getIntegerStr()) * XX_CARRY + calcData.getInNumber() :
                                Long.parseLong(calcData.getIntegerStr()) * XX_CARRY - calcData.getInNumber()));//add integer
                    }
                    CalcShowStr = calcData.getIntegerStr();
                }
                calcData.setIntermediateOne(new BigDecimal(calcData.getIntegerStr() + "." + calcData.getDecimalStr()));
            } else if (calcData.getOperaCounts() == CalcData.OPERA_CNT_MULTI) {
                switch (calcData.getCalcOperaTypes()) {
                    case ADD:
                    case SUB:
                    case MUL:
                    case DIV:
                    case EQL:
                        newMathOpera = calcData.getCalcOperaTypes();
                        equleCalculation(calcData);
                        break;
                }
                calcData.setIntegerStr("0");
                calcData.setDecimalStr("");
                editbal = false;
                calcData.setDotStatus(false);
                if (illegalStatus) {
                    CalcShowStr = "Illegal zero!";
                    return;
                }
                calcData.setIntermediateTwo(calcData.getResultValue());
                CalcShowStr = calcData.getResultValue().toString();
            } else {
                switch (calcData.getCalcOperaTypes()) {
                    case EQL:
                        if (newMathOpera != CalcOperaTypes.EQL && nowOpera == CalcOperGroups.CALC && foreOpera == nowOpera) { // 5 * 6 - =_
                            preMathOpera = newMathOpera;  // use the calc operation of last time
                            calcData.setIntermediateOne(calcData.getIntermediateTwo());
                        }
                        equleCalculation(calcData);
                        break;
                    case ADD:
                    case SUB:
                    case MUL:
                    case DIV:
                        break;
                }
                calcResetInput(calcData);
                calcData.setIntermediateTwo(calcData.getResultValue());
                newMathOpera = calcData.getCalcOperaTypes();
                CalcShowStr = calcData.getResultValue().toString();
            }
        }
    }

    private void calcResetResult(CalcData calcData) {
        preMathOpera = CalcOperaTypes.EQL;
        newMathOpera = CalcOperaTypes.EQL;
        calcData.setResultValue(new BigDecimal("0"));
        calcData.setIntermediateOne(new BigDecimal("0"));
        calcData.setIntermediateTwo(new BigDecimal("0"));
    }

    private void calcResetInput(CalcData calcData) {
        calcData.setIntegerStr("0");
        calcData.setDecimalStr("");
        calcData.setDotStatus(false);
        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
    }

    private void foreOperaCalcCheck(CalcData calcData) {
        if (foreOpera == CalcOperGroups.CALC) {
            calcData.setIntermediateOne(calcData.getIntermediateTwo());
            preMathOpera = newMathOpera;
        }
    }

    private void equleCalculation(CalcData calcData) {
        switch (preMathOpera) {
            case ADD:
                calcData.setResultValue(calcData.getIntermediateTwo().add(calcData.getIntermediateOne()));
                break;
            case SUB:
                calcData.setResultValue(calcData.getIntermediateTwo().subtract(calcData.getIntermediateOne()));
                break;
            case DIV:
                try {
                    calcData.setResultValue(calcData.getIntermediateTwo().divide(calcData.getIntermediateOne(), MAX_LENGTH, BigDecimal.ROUND_CEILING));
                } catch (ArithmeticException e) {
                    if (calcData.getIntermediateOne().intValue() == 0) {
                        illegalStatus = true;
                        return;
                    }
                }
                break;
            case MUL:
                calcData.setResultValue(calcData.getIntermediateTwo().multiply(calcData.getIntermediateOne()));
                break;
            case NUL:
            case EQL:
                calcData.setIntermediateTwo(calcData.getIntermediateOne());
                calcData.setResultValue(calcData.getIntermediateTwo());
                break;
        }
//        calcData.setIntermediateOne(calcData.getResultValue());
        // 1. Cut decimal scale to 16
        // 2. Cut "000" string away
        String tmpString = calcData.getResultValue().setScale(MAX_LENGTH, BigDecimal.ROUND_CEILING).toPlainString();
        calcData.setResultValue(new BigDecimal(deleteExtraZeros(tmpString)));
//        calcData.setResultValue(new BigDecimal(calcData.getResultValue().setScale(MAX_LENGTH, BigDecimal.ROUND_CEILING).stripTrailingZeros().toPlainString()));

    }

    public String getCalcShowStr() {
        String tmpStr;
        if (CalcShowStr.charAt(0) != '-' && CalcShowStr.charAt(0) != ' ') {
            CalcShowStr = " " + CalcShowStr;
        }
        if (CalcShowStr.contains(".")) {
            if (CalcShowStr.length() <= MAX_LENGTH + 1) {
                return CalcShowStr;
            } else {
                tmpStr = CalcShowStr.substring(0, MAX_LENGTH + 1);
            }
        } else {
            if (CalcShowStr.length() < MAX_LENGTH + 1) {
                return CalcShowStr;
            } else {
                tmpStr = CalcShowStr.substring(0, MAX_LENGTH + 1);
            }
        }
        return deleteExtraZeros(tmpStr);
    }

    //    /**
//     * 清零
//     * @param str 原始字符串
//     * @return
//     */
//    public static String trim(String str) {
//        if (str.indexOf(".") != -1 && str.charAt(str.length() - 1) == '0') {
//            return trim(str.substring(0, str.length() - 1));
//        } else {
//            return str.charAt(str.length() - 1) == '.' ? str.substring(0, str.length() - 1) : str;
//        }
//    }
    public static String deleteExtraZeros(String str) {
        if (str.contains(".")) {
            for (int i = str.length() - 1; i >= 0; i--) {
                if (str.charAt(i) == '.') {
                    return str.substring(0, i);
                } else if (str.charAt(i) != '0') {
                    return str.substring(0, i + 1);
                }
            }
        }
        return str;
    }

    public static BigDecimal BigDecimalSqrt(BigDecimal A, final int SCALE) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, SCALE, BigDecimal.ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(new BigDecimal("2"), SCALE, BigDecimal.ROUND_HALF_UP);
        }
        return x1;
    }

    public static String getDecimalStr(String str) {
        if (str.contains(".")) {
            return str.substring(str.indexOf(".") + 1);
        } else {
            return "";
        }
    }
}
