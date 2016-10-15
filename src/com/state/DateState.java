package com.state;

/**
 * Created by lgluo on 2016/10/15.
 */
public enum DateState {
    NO_DATE(0),
    YYYYMMDD(1),
    MMDDYYYY(2),
    DDMMYYYY(3),
    YYMMDD(4),
    MMDDYY(5),
    DDMMYY(6),
    TEMP(7);

    private int date;

    private DateState(int d) {
        date = d;
    }

    public int getDate(){
        return date;
    }
}
