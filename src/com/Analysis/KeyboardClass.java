package com.Analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lgluo on 2016/10/14.
 */
public class KeyboardClass {
    private static Map<Character, List<Integer>> keyboard = new HashMap();

    private static KeyboardClass keyboardClass;

    private KeyboardClass(){
        String string1 = "`1234567890-=~!@#$%^&*()_+";
        for(int i = 0; i < string1.length()/2; i++) {
            List<Integer> position = new ArrayList<>();
            position.add(i - 1);
            position.add(0);
            keyboard.put(string1.charAt(i), position);
            keyboard.put(string1.charAt(i + string1.length()/2), position);
        }
        String string2 = "qwertyuiop[]\\QWERTYUIOP{}|";
        for(int i = 0; i < string2.length()/2; i++) {
            List<Integer> position = new ArrayList<>();
            position.add(i);
            position.add(1);
            keyboard.put(string2.charAt(i), position);
            keyboard.put(string2.charAt(i + string2.length()/2), position);
        }
        String string3 = "asdfghjkl;'ASDFGHJKL:\"";
        for(int i = 0; i < string3.length()/2; i++) {
            List<Integer> position = new ArrayList<>();
            position.add(i);
            position.add(2);
            keyboard.put(string3.charAt(i), position);
            keyboard.put(string3.charAt(i + string3.length()/2), position);
        }
        String string4 = "zxcvbnm,./ZXCVBNM<>?";
        for(int i = 0; i < string4.length()/2; i++) {
            List<Integer> position = new ArrayList<>();
            position.add(i);
            position.add(3);
            keyboard.put(string4.charAt(i), position);
            keyboard.put(string4.charAt(i + string4.length()/2), position);
        }
    }

    public static KeyboardClass getInstance(){
        if(keyboardClass == null) {
            keyboardClass = new KeyboardClass();
        }
        return keyboardClass;
    }

    public List<Integer> getPosition(char c) {
        return keyboard.get(c);
    }

    public boolean isAdjacent(char pos1, char pos2){
        boolean adjacent = false;
        List<Integer> position1 = keyboard.get(pos1);
        List<Integer> position2 = keyboard.get(pos2);
        if((Math.abs(position1.get(0) - position2.get(0)) <= 1) && (Math.abs(position1.get(1) - position2.get(1)) <= 1)) {
            adjacent = true;
        }
        return adjacent;
    }

    public boolean isSameRow(char pos1, char pos2) {
        return (keyboard.get(pos1).get(1).equals(keyboard.get(pos2).get(1)));
    }
}
