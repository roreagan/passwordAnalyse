package com.state;

/**
 * Created by lgluo on 2016/10/15.
 */
public enum DateState {
    NO_DATE(0),
    YYYYMMDD(1),
    MMDDYYYY(2),
    DDMMYYYY(3),
    YYMMDD(11),
    MMDDYY(12),
    DDMMYY(13),
    TEMP(100);

    private int date;

    private DateState(int d) {
        date = d;
    }

    public int getDate(){
        return date;
    }
}
