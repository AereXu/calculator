package com.purify.aerexu.calculator.entity;

/**
 * Created by AereXu on 2015/8/30.
 */
enum CalcOperGroups {
    NUMB,
    CALC,
    MIDD,
    EDIT,
    LEFT;
    static CalcOperGroups calcType2Group(CalcOperaTypes types){
        switch (types){
            case ONE:
            case TWO:
            case THR:
            case FOU:
            case FIV:
            case SIX:
            case SEV:
            case EIG:
            case NIN:
            case ZER:
                return NUMB;
            case ADD:
            case SUB:
            case DIV:
            case MUL:
            case EQL:
                return CALC;
            case SQR:
            case PER:
            case REC:
                return MIDD;
            case SGN:
            case DOT:
            case DEL:
                return EDIT;
            case CLE:
            case CLR:
            case NUL:
            default:
                return LEFT;
        }
    }
}
