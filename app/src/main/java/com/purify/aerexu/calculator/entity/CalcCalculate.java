package com.purify.aerexu.calculator.entity;

import java.math.BigDecimal;

/**
 * Created by AereXu on 2015/8/23.
 */
public class CalcCalculate {
    public static final int MAX_LENGTH = 32;
    public static final long XX_CARRY = 10;
    private String CalcShowStr;
    private String IntermediateOneStr;
    private String IntermediateTwoStr;
    private String CalcOperatorStr;
    private CalcOperaTypes preMathOpera;
    private CalcOperaTypes newMathOpera;
    private CalcOperGroups foreOpera;
    private CalcOperGroups nowOpera;
    private boolean illegalStatus;
    private boolean editbal;

    public CalcCalculate() {
        CalcShowStr = "";
        IntermediateOneStr = "0";
        IntermediateTwoStr = "0";
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
                        IntermediateOneStr = CalcShowStr;
                        break;
                    case SQR:
                        foreOperaCalcCheck(calcData);
                        if (calcData.getIntermediateOne().compareTo(new BigDecimal("0")) < 0) {
                            illegalStatus = true;
                            CalcShowStr = "Illegal input!";
                            return;
                        }
                        CalcOperatorStr = "sqrt(" + calcStringFormat(calcData.getIntermediateOne().toString(), MAX_LENGTH / 2) + " )";
                        calcData.setIntermediateOne(BigDecimalSqrt(calcData.getIntermediateOne(), MAX_LENGTH));
                        calcResetInput(calcData);
                        CalcShowStr = calcData.getIntermediateOne().toString();
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        break;
                    case PER:
                        foreOperaCalcCheck(calcData);
                        CalcOperatorStr = calcStringFormat(calcData.getIntermediateOne().toString(), MAX_LENGTH / 5) +
                                "×" + calcStringFormat(calcData.getIntermediateTwo().toString(), MAX_LENGTH / 5) + "/100";
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
                            String tmpStr = calcData.getIntermediateOne().toString();
                            calcData.setIntermediateOne(new BigDecimal("1").divide(calcData.getIntermediateOne(), MAX_LENGTH, BigDecimal.ROUND_CEILING).stripTrailingZeros());
                            CalcOperatorStr = "reciproc(" + calcStringFormat(tmpStr, MAX_LENGTH / 2) + " )";
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
                        IntermediateOneStr = CalcShowStr;
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
                            IntermediateOneStr = CalcShowStr;
                        }
                        break;
                    case CLE:
                        calcResetInput(calcData);
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        calcData.setIntermediateOne(new BigDecimal("0"));
                        CalcShowStr = "0";
                        IntermediateOneStr = "0";
                        refreshMathOperaStr(preMathOpera);
                        break;
                    case CLR:
                        calcResetResult(calcData);
                        calcResetInput(calcData);
                        calcData.setOperaCounts(CalcData.OPERA_CNT_NONE);
                        CalcOperatorStr = " ";
                        IntermediateOneStr = " ";
                        IntermediateTwoStr = " ";
                        CalcShowStr = calcData.getIntermediateOne().toString();
                        break;
                }
            } else if (calcData.getOperaCounts() == CalcData.OPERA_CNT_NONE) {
                if (newMathOpera == CalcOperaTypes.EQL && foreOpera != CalcOperGroups.MIDD) {
                    calcData.setIntegerStr("0");
                    calcData.setDecimalStr("");
                    newMathOpera = CalcOperaTypes.NUL;
                    CalcOperatorStr = " ";
                    IntermediateTwoStr = " ";
                }
                if (newMathOpera != CalcOperaTypes.EQL) {
                    preMathOpera = newMathOpera;
                }
                if(newMathOpera != CalcOperaTypes.NUL ){
                    refreshMathOperaStr(preMathOpera);
                    IntermediateTwoStr = calcData.getIntermediateTwo().toPlainString();
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
                IntermediateOneStr = CalcShowStr;
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
                refreshMathOperaStr(calcData.getCalcOperaTypes() == CalcOperaTypes.EQL ? preMathOpera : calcData.getCalcOperaTypes());
                if (illegalStatus) {
                    return;
                }
                calcResetInput(calcData);
                calcData.setIntermediateTwo(calcData.getResultValue());
                CalcShowStr = calcData.getResultValue().toString();
            } else {
                switch (calcData.getCalcOperaTypes()) {
                    case EQL:
                        if (newMathOpera != CalcOperaTypes.EQL && nowOpera == CalcOperGroups.CALC && foreOpera == nowOpera) { // 5 * 6 - =_
                            preMathOpera = newMathOpera;  // use the calc operation of last time
                            calcData.setIntermediateOne(calcData.getIntermediateTwo());
                        }
                        refreshMathOperaStr(preMathOpera);
                        equleCalculation(calcData);
                        break;
                    case ADD:
                        CalcOperatorStr = "+";
                        break;
                    case SUB:
                        CalcOperatorStr = "-";
                        break;
                    case MUL:
                        CalcOperatorStr = "×";
                        break;
                    case DIV:
                        CalcOperatorStr = "÷";
                        break;
                }
                if (illegalStatus) {
                    return;
                }
                calcResetInput(calcData);
                calcData.setIntermediateTwo(calcData.getResultValue());
                newMathOpera = calcData.getCalcOperaTypes();
                CalcShowStr = calcData.getResultValue().toString();
            }
        }
    }

    private void refreshMathOperaStr(CalcOperaTypes type) {
        switch (type) {
            case ADD:
                CalcOperatorStr = "+";
                break;
            case SUB:
                CalcOperatorStr = "-";
                break;
            case MUL:
                CalcOperatorStr = "×";
                break;
            case DIV:
                CalcOperatorStr = "÷";
                break;
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
        editbal = false;
    }

    private void foreOperaCalcCheck(CalcData calcData) {
        if (foreOpera == CalcOperGroups.CALC) {
            calcData.setIntermediateOne(calcData.getIntermediateTwo());
            if (newMathOpera != CalcOperaTypes.EQL && newMathOpera != CalcOperaTypes.NUL)
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
                        CalcShowStr = "Illegal zero!";
                        return;
                    }
                }
                break;
            case MUL:
                calcData.setResultValue(calcData.getIntermediateTwo().multiply(calcData.getIntermediateOne()));
                if (calcData.getResultValue().compareTo(new BigDecimal("99999999999999999999999999999999")) > 0
                        || calcData.getResultValue().compareTo(new BigDecimal("-99999999999999999999999999999999")) < 0) {
                    illegalStatus = true;
                    CalcShowStr = "Exceed number range!";
                    return;
                }
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
        IntermediateOneStr = calcData.getIntermediateOne().toPlainString();
        IntermediateTwoStr = calcData.getIntermediateTwo().toPlainString();
    }

    public String getCalcOperatorStr() {
        return CalcOperatorStr;
    }

    public String getCalcShowStr() {
        return calcStringFormat(CalcShowStr, MAX_LENGTH);
    }

    public String getIntermediateOneStr() {
        return calcStringFormat(IntermediateOneStr, MAX_LENGTH / 2);
    }

    public String getIntermediateTwoStr() {
        return calcStringFormat(IntermediateTwoStr, MAX_LENGTH / 2);
    }

    private String calcStringFormat(String string, int maxLength) {
        String tmpStr;
        if (string.charAt(0) != '-' && string.charAt(0) != ' ') {
            string = " " + string;
        }
        if (string.contains(".")) {
            if (string.length() <= maxLength + 1) {
                return string;
            } else {
                tmpStr = string.substring(0, maxLength + 1);
            }
        } else {
            if (string.length() < maxLength + 1) {
                return string;
            } else {
                tmpStr = string.substring(0, maxLength + 1);
            }
        }
        return deleteExtraZeros(tmpStr);
    }


    //    /**
//     * 清零
//     * @param str 原始字符�
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
