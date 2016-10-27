package com.state;

/**
 * Created by lgluo on 2016/10/15.
 */
public enum DatePattern {
    DIGIT_ONLY(0),
    LETTER_DIGIT(1),
    SYMBOL_DIGIT(2),
    LETTER_DIGIT_SYMBOL(3);

    private int datePattern;

    private DatePattern(int datePattern) {
        this.datePattern = datePattern;
    }

    public int getDatePattern() {
        return datePattern;
    }
}
