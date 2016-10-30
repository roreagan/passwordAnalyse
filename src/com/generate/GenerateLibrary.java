package com.generate;

import com.Analysis.KeyboardClass;
import com.Analysis.WordsMatch;
import com.state.KeyboardState;

import java.io.*;
import java.time.Month;
import java.util.*;

/**
 * Created by lgluo on 2016/10/27.
 */
public class GenerateLibrary {
    private  final int passwordNum = 1000;
    private  double[] mode = {0, 0, 0, 0};
    //these arrays denote the arrange of every pattern, eg: [0.1, 0.3, 0.5] denotes the four patterns lie in [0,0.1], (0.1, 0.3], (0.3, 0.5], (0.5, 1]
    //relate to KeyboardState without no_pattern
    private  double[] keyboardPattern = {0, 0, 0};
    //wordPro denotes the probability of pinyin and english words
    private  double wordsPro =  0.0;
    //relate to DateState without on_date and temp
    private  double[] datePattern = {0, 0, 0, 0, 0};
    //choose the top 10(or 20 or more) popular password pattern
    private  Map<String, Double> passwordPattern = new HashMap<>();
    private  Map<Character, Double> symbolList = new HashMap<>();
    private  Map<Character, Double> lowercaseList = new HashMap<>();
    private  Map<Character, Double> uppercaseList = new HashMap<>();
    private  Map<Character, Double> digitList = new HashMap<>();

    private String libName = "";
//    private static GenerateLibrary instance = null;
//
//    public static GenerateLibrary getInstance() {
//        if(instance == null) {
//            instance = new GenerateLibrary();
//        }
//        return instance;
//    }

    public GenerateLibrary(String filename) {
        libName = filename;
        File file = new File(filename + ".log");
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            int lineNum = 0;
            int total = 0;
            String charsBuffer = null;
            int lowercaseNum = 0;
            int uppercaseNum = 0;
            int digitNum = 0;
            int symbolNum = 0;

            int keyboardTotal = 0;
            int dateTotal = 0;
            int wordTotal = 0;
            String readBuffer = null;
            while((readBuffer = reader.readLine()) != null) {
                lineNum++;
                switch (lineNum) {
                    case 2:
                        total = Integer.valueOf(readBuffer);
                        break;
                    case 4:
                        charsBuffer = readBuffer;
                        break;
                    case 5:
                        lowercaseNum = Integer.valueOf(readBuffer);
                        break;
                    case 6:
                        uppercaseNum = Integer.valueOf(readBuffer);
                        break;
                    case 7:
                        digitNum = Integer.valueOf(readBuffer);
                        break;
                    case 8:
                        symbolNum = Integer.valueOf(readBuffer);
                        String[] charList = charsBuffer.split("    ");
                        for(int i = 0; i < charList.length; i++) {
                            if (charList[i].charAt(0) != ':') {
                                String[] temp = charList[i].split(":");
                                char c = temp[0].charAt(0);
                                if (c >= 97 && c <= 122) {
                                    lowercaseList.put(c, (double) Integer.valueOf(temp[1]) / lowercaseNum);
                                } else if (c >= 65 && c <= 90) {
                                    uppercaseList.put(c, (double) Integer.valueOf(temp[1]) / uppercaseNum);
                                } else if (c >= 48 && c <= 57) {
                                    digitList.put(c, (double) Integer.valueOf(temp[1]) / digitNum);
                                } else {
                                    symbolList.put(c, (double) Integer.valueOf(temp[1]) / symbolNum);
                                }
                            }
                        }
                        break;
                    case 10:
                        String[] tempPatterns = readBuffer.split("    ");
                        int patternTotal = 0;
                        for (String patternNum: tempPatterns) {
                            String[] temp = patternNum.split(":");
                            patternTotal += Integer.valueOf(temp[1]);
                        }
                        for (String patternNum: tempPatterns) {
                            String[] temp = patternNum.split(":");
                            passwordPattern.put(temp[0], (double)Integer.valueOf(temp[1])/patternTotal);
                        }
                        break;
                    case 14:
                        String[] tempKeyboard = readBuffer.split("    ");
                        for(int i = 1; i < tempKeyboard.length; i++) {
                            String[] temp = tempKeyboard[i].split(":");
                            keyboardTotal += Integer.valueOf(temp[1]);
                        }
                        double keyboardPro = 0.0;
                        for(int i = 0; i < keyboardPattern.length; i++) {
                            String[] temp = tempKeyboard[i].split(":");
                            keyboardPro += (double)Integer.valueOf(temp[1])/keyboardTotal;
                            keyboardPattern[i] = keyboardPro;
                        }
                        break;
                    case 16:
                        String[] tempDate = readBuffer.split("    ");
                        for(int i = 1; i < tempDate.length; i++) {
                            String[] temp = tempDate[i].split(":");
                            dateTotal += Integer.valueOf(temp[1]);
                        }
                        double datePro = 0.0;
                        for(int i = 0; i < keyboardPattern.length; i++) {
                            String[] temp = tempDate[i].split(":");
                            datePro += (double)Integer.valueOf(temp[1])/dateTotal;
                            datePattern[i] = datePro;
                        }
                        break;
                    case 18:
                        String[] tempDateTotal = readBuffer.split("    ");
                        dateTotal = Integer.valueOf(tempDateTotal[0].split(":")[1]);
                        break;
                    case 24:
                        String[] tempWords = readBuffer.split("    ");
                        wordTotal = Integer.valueOf(tempWords[0].split(":")[1]) + Integer.valueOf(tempWords[1].split(":")[1]);
                        wordsPro = (double)Integer.valueOf(tempWords[0].split(":")[1])/wordTotal;
                        mode[0] = (double)keyboardTotal / total;
                        mode[1] = (double)wordTotal / total;
                        mode[2] = (double)dateTotal / total;
                        mode[3] = 1.0 - mode[0] - mode[1] - mode[2];
                        break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateKeyboardPassword() {
        double random = Math.random();
        KeyboardState state = KeyboardState.NO_PATTERN;
        if(random < keyboardPattern[0]) {
            state = KeyboardState.SAME_ROW;
        } else if(random < keyboardPattern[1]) {
            state = KeyboardState.ZIG_ZAG;
        } else if(random < keyboardPattern[2]) {
            state = KeyboardState.SNAKE;
        } else {
            state = KeyboardState.SAME_ROW_NUMBER_ONLY;
        }
        return KeyboardClass.getInstance().generatePassword(state);
    }

    private String generateWordsPassword() {
        return WordsMatch.getInstance().generatePassword(Math.random() < wordsPro);
    }

    private String generateDatePassword() {
        String result = "";
        Random ra = new Random();
        String year = String.valueOf(ra.nextInt(200) + 1900);
        String month = String.valueOf(ra.nextInt(12) + 1);
        String day = String.valueOf(ra.nextInt(31) + 1);
        double random = Math.random();
        if(random < datePattern[0]) {
            result = year + month + day;
        } else if(random < datePattern[1]) {
            result = month + day + year;
        } else if (random < datePattern[2]) {
            result = day + month + year;
        } else if(random < datePattern[3]){
            result = year.substring(2) + month + day;
        } else if(random < datePattern[4]) {
            result = month + day + year.substring(2);
        } else {
            result = day + month + year;
        }
        return result;
    }

    private String generatePatternPassword() {
        double random = Math.random();
        double prob = 0.0;
        String pattern = "";
        for(Map.Entry entry : passwordPattern.entrySet()) {
            prob += (Double) entry.getValue();
            if(random <= prob) {
                pattern = (String)entry.getKey();
                break;
            }
        }
        String result = "";
        for(int i = 0; i < pattern.length(); i++) {
            Map<Character, Double> charMap = symbolList;
            switch (pattern.charAt(i)) {
                case 'D':
                    charMap = digitList;
                    break;
                case 'L':
                    charMap = lowercaseList;
                    break;
                case 'U':
                    charMap = uppercaseList;
                    break;
            }
            random = Math.random();
            prob = 0.0;
            for(Map.Entry entry : charMap.entrySet()) {
                prob += (Double) entry.getValue();
                if(random <= prob) {
                    result += (Character)entry.getKey();
                    break;
                }
            }
        }
        return result;
    }

    public void generatePassword() {
        File file = new File(libName + "Lib.txt");
        BufferedWriter writer = null;
        try{
            String content = "";
            writer = new BufferedWriter(new FileWriter(file));
            for(int i = 0; i < passwordNum; i++) {
                double random = Math.random();
                double pro = 0.0;
                for(int j = 0; j < mode.length; j++) {
                    pro += mode[j];
                    if(random <= pro) {
                        switch (j) {
                            case 0: content += generateKeyboardPassword() + "\n";break;
                            case 1: content += generateWordsPassword() + "\n";break;
                            case 2: content += generateDatePassword() + "\n";break;
                            case 3: content += generatePatternPassword() + "\n";break;
                        }
                    }
                }
            }
            writer.write(content);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        GenerateLibrary.getInstance().generatePassword();
//    }

}
