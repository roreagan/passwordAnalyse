package com.Analysis;

import com.state.KeyboardState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgluo on 2016/10/14.
 */
public class Analyse {

    public void characterDistribution(String password){
        Map<Character, Integer> characterMap = new HashMap<>();
        for(int i = 0; i < password.length(); i++) {
            char temp = password.charAt(i);
            if(characterMap.get(temp) == null) {
                int num = characterMap.get(temp) == null ? 1 : characterMap.get(temp)+1;
                characterMap.put(temp, num);
            } else {
                characterMap.put(temp,  characterMap.get(temp)+1);
            }
        }
        //todo: transform the map to the database
    }

    public void structures(String password){
        String structure = "";
        for(int temp = 0; temp < password.length(); temp++) {
            char i = password.charAt(temp);
            if(i >= 48 && i <= 57) {
                structure += "D";
            } else if(i >= 65 && i <= 90) {
                structure += "H";
            } else if(i >= 97 && i <= 122) {
                structure += "L";
            } else {
                structure += "S";
            }
        }
        //todo: transform the structure to the database
    }

    public int keyboardPattern(String password){
        KeyboardState result = KeyboardState.NO_PATTERN;
        boolean sameRow = true;
        boolean zigZag = true;
        for(int i = 1; i < password.length(); i++) {
            char pos1 = password.charAt(i-1);
            char pos2 = password.charAt(i);
            if(KeyboardClass.getInstance().isAdjacent(pos1, pos2)) {
                sameRow = sameRow & KeyboardClass.getInstance().isSameRow(pos1, pos2);
                zigZag = zigZag & !KeyboardClass.getInstance().isSameRow(pos1,pos2);
            } else {
                return result.getState();
            }
        }
        if(sameRow) {
            if(KeyboardClass.getInstance().getPosition(password.charAt(0)).get(1) == 0) {
                result = KeyboardState.SAME_ROW_NUMBER_ONLY;
            } else {
                result = KeyboardState.SAME_ROW;
            }
        } else if(zigZag) {
            result = KeyboardState.ZIG_ZAG;
        } else {
            result = KeyboardState.SNAKE;
        }
        return result.getState();
        //todo: transform the result to the database
    }

    public void wordsAnalyse(String password, String language) {
        //convert the input to letter-only
        String input = "";
        password = password.toLowerCase();
        for(int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if(c >= 97 && c <= 122) {
                input += c;
            }
        }
        WordsMatch.getInstance().identifyWord(input, language);
        //todo: transform the result to the database
    }

    public void dataFormat(String password) {
        //find the consecutive numbers of exactly six or eight digits
        String date = "";
        int result = 0;
        //todo: analyse the composition of the password containing date
        for(int i = 0; i < password.length(); i++) {
            String temp = "";
            int j = i;
            for(; j < password.length(); j++) {
                char tempc = password.charAt(j);
                if(tempc < 48 || tempc > 57) {
                    break;
                }
                temp += tempc;
            }
            if(temp.length() == 6 || temp.length() == 8) {
                date = temp;
                break;
            } else {
                i = j;
            }
        }
        if(date.length() == 6 || date.length() == 8) {
            result = DateFormat.getInstance().dateAnalyse(date);
        }
        //todo: transform the result to the database
        //todo: finally we need to deal with the temp data
    }


    public static void main(String args[]){
        Analyse analyse = new Analyse();
        analyse.dataFormat("ab19950222");
    }
}
