package com.state;

/**
 * Created by lgluo on 2016/10/15.
 */
public enum KeyboardState {
    //0: no pattren; 1: same row; 2: zig zag; 3: snake; 4: same row and number-only
    NO_PATTERN(0),
    SAME_ROW(1),
    ZIG_ZAG(2),
    SNAKE(3),
    SAME_ROW_NUMBER_ONLY(4);

    private int state;

    private KeyboardState(int s){
        state = s;
    }

    public int getState() {
        return state;
    }


}
