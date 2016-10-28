package com.database;

import com.Analysis.WordNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by lgluo on 2016/10/15.
 */
public class ResultOperator {
    private final int top = 30;
    private WordNode passwordNode = new WordNode();
    private WordNode structureNode = new WordNode();
    private WordNode englishNode = new WordNode();
    private WordNode pinyinNode = new WordNode();

    private Map<String, Integer> passwordMap = new HashMap<>();
    private Map<String, Integer> structureMap = new HashMap<>();
    private Map<String, Integer> englishMap = new HashMap<>();
    private Map<String, Integer> pinyinMap = new HashMap<>();

    private Map<Character, Integer> characterIntegerMap = new HashMap();
    //relate to KeyboardState
    private long[] keyboardPattern = {0, 0, 0, 0, 0};
    //0: pinyin_letter only   1: english_letter only   2: pinyin_mix    3:english_mix
    private long[] wordsPattern = {0, 0, 0, 0};
    //relate to DateState
    private long[] datePattern = {0, 0, 0, 0, 0, 0, 0};
    //relate to DatePattern
    private long[] passDatePattern = {0, 0, 0, 0};
    //lowercase and uppercase in pinyin or words, cases[0] for pinyin and cases[1] for words
    private long[] cases = {0, 0};

    private long passwordNum = 0;

//    private static ResultOperator resultOperator = null;
//
//    private ResultOperator() {
//
//    }
//
//    public static ResultOperator getInstance() {
//        if(resultOperator == null) {
//            resultOperator = new ResultOperator();
//        }
//        return resultOperator;
//    }

    //各个字符的使用频率
    public void analyseCharacter(Map<Character, Integer> map) {
        passwordNum++;
        Iterator entries = map.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry entry = (Map.Entry)entries.next();
            char key = (Character) entry.getKey();
            int value = (Integer) entry.getValue();
            if(characterIntegerMap.get(key) != null) {
                characterIntegerMap.put(key, characterIntegerMap.get(key) + value);
            } else {
                characterIntegerMap.put(key, value);
            }
        }
    }


    public void add(String string, String nodeType) {
        WordNode node = chooseNode(nodeType);
        for(int i = 0; i < string.length(); i++) {
            if(node.nodeList.get(string.charAt(i)) != null) {
                node = node.nodeList.get(string.charAt(i));
            } else {
                node.nodeList.put(string.charAt(i), new WordNode(string.charAt(i)));
                node = node.nodeList.get(string.charAt(i));
            }
        }
        node.isValue = true;
        node.num++;
    }

    public Map<String, Integer> findTop10(String nodeType) {
        WordNode node = chooseNode(nodeType);
        Map<String, Integer> map = chooseMap(nodeType);
        searchTrie(node, "", nodeType);
        return map;
    }

    private void searchTrie(WordNode node, String string, String nodeType) {
        for(WordNode tempNode : node.nodeList.values()) {
            searchTrie(tempNode, string + tempNode.c, nodeType);
        }
        if(node.isValue) {
            Map<String, Integer> map = chooseMap(nodeType);
            if(map.size() < top) {
                map.put(string, node.num);
            } else {
                String minKey = string;
                int minValue = node.num;
                Iterator entries = map.entrySet().iterator();
                while(entries.hasNext()) {
                    Map.Entry entry = (Map.Entry)entries.next();
                    String key = (String) entry.getKey();
                    int value = (Integer) entry.getValue();
                    if(value < minValue) {
                        minKey = key;
                        minValue = value;
                    }
                }
                if(!minKey.equals(string)) {
                    map.remove(minKey);
                    map.put(string, node.num);
                }
            }
        }
    }

    private WordNode chooseNode(String nodeType) {
        WordNode node;
        switch (nodeType) {
            case "password":
                node = passwordNode;
                break;
            case "structure":
                node = structureNode;
                break;
            case "english":
                node = englishNode;
                break;
            case "pinyin":
                node = pinyinNode;
                break;
            default:
                node = new WordNode();
        }
        return node;
    }

    private Map<String, Integer> chooseMap(String nodeType) {
        Map<String, Integer> map;
        switch (nodeType) {
            case "password":
                map = passwordMap;
                break;
            case "structure":
                map = structureMap;
                break;
            case "english":
                map = englishMap;
                break;
            case "pinyin":
                map = pinyinMap;
                break;
            default:
                map = new HashMap();
        }
        return map;
    }

    public void addKeyboardPattern(int patterm) {
        keyboardPattern[patterm]++;
    }

    public void addWordsPattern(boolean isPinyin, boolean isEnglish, boolean letterOnly, String word, boolean isUppercase) {
        if(isPinyin || isEnglish){
            if(isPinyin) {
                if(letterOnly) {
                    wordsPattern[0]++;
                } else {
                    wordsPattern[2]++;
                }
                cases[0] += isUppercase? 1: 0;
                add(word, "pinyin");
            } else {
                if(letterOnly) {
                    wordsPattern[1]++;
                } else {
                    wordsPattern[3]++;
                }
                cases[1] += isUppercase? 1: 0;
                add(word, "english");
            }
        }
    }

    public void addDatePattern(int pattern, int passPattern) {
        datePattern[pattern]++;
        if(pattern != 0) {
            passDatePattern[passPattern]++;
        }
    }

    public void addDatePattern(int passPattern) {
        passDatePattern[passPattern]++;
    }

    public void addRestDatePattern(long[] patterns) {
        datePattern[1] += patterns[0] * datePattern[1] / (datePattern[1] + datePattern[2]) + patterns[1] * datePattern[1] / (datePattern[1] + datePattern[3]) +  patterns[3] * datePattern[1] / (datePattern[1] + datePattern[2] + datePattern[3]);
        datePattern[2] += patterns[0] * datePattern[2] / (datePattern[1] + datePattern[2]) + patterns[2] * datePattern[2] / (datePattern[2] + datePattern[3]) +  patterns[3] * datePattern[2] / (datePattern[1] + datePattern[2] + datePattern[3]);
        datePattern[3] += patterns[1] * datePattern[3] / (datePattern[1] + datePattern[3]) + patterns[2] * datePattern[3] / (datePattern[2] + datePattern[3]) +  patterns[3] * datePattern[3] / (datePattern[1] + datePattern[2] + datePattern[3]);
        datePattern[4] += patterns[4] * datePattern[4] / (datePattern[4] + datePattern[5]) + patterns[5] * datePattern[4] / (datePattern[4] + datePattern[6]) +  patterns[7] * datePattern[1] / (datePattern[4] + datePattern[5] + datePattern[6]);
        datePattern[5] += patterns[4] * datePattern[5] / (datePattern[4] + datePattern[5]) + patterns[6] * datePattern[5] / (datePattern[5] + datePattern[6]) +  patterns[7] * datePattern[2] / (datePattern[4] + datePattern[5] + datePattern[6]);
        datePattern[6] += patterns[5] * datePattern[6] / (datePattern[4] + datePattern[6]) + patterns[6] * datePattern[6] / (datePattern[5] + datePattern[6]) +  patterns[7] * datePattern[3] / (datePattern[4] + datePattern[5] + datePattern[6]);
    }

    public void output(String filename) {
        if(filename.contains(".")) {
            filename = filename.split(".")[0];
        }
        File file = new File(filename + ".log");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            String result = "总共分析的密码数\n" +  passwordNum + "\n";;
            //密码构成元素分析：
            //1.密码构成元素分析（各个字符的使用频率）；
            result += "字符的使用频率\n";
            int lowercase = 0;
            int uppercase = 0;
            int digit = 0;
            int symbol = 0;
            Iterator charEntries = characterIntegerMap.entrySet().iterator();
            while(charEntries.hasNext()) {
                Map.Entry entry = (Map.Entry)charEntries.next();
                Character key = (Character) entry.getKey();
                int value = (Integer) entry.getValue();
                result += key + ":" + value + "    ";
                if(key >= 97 && key <= 122) {
                    lowercase += value;
                } else if(key >= 65 && key <= 90) {
                    uppercase += value;
                } else if(key >= 48 && key <=57) {
                    digit += value;
                } else {
                    symbol += value;
                }
            }
            result += "\n";
            result += lowercase + "\n";
            result += uppercase + "\n";
            result += digit + "\n";
            result += uppercase + "\n";
            //2.密码结构分析
            result += "密码结构Top10\n";
            Map<String, Integer> structureReusltMap = findTop10("structure");
            Iterator structureEntries = structureMap.entrySet().iterator();
            while(structureEntries.hasNext()) {
                Map.Entry entry = (Map.Entry)structureEntries.next();
                String key = (String) entry.getKey();
                int value = (Integer) entry.getValue();
                result += key + ":" + value + "    ";
            }
            result += "\n";
            //3.常用密码Top10:
            result += "常用密码Top10\n";
            Map<String, Integer> passwordReusltMap = findTop10("password");
            Iterator passwordEntries = passwordMap.entrySet().iterator();
            while(passwordEntries.hasNext()) {
                Map.Entry entry = (Map.Entry)passwordEntries.next();
                String key = (String) entry.getKey();
                int value = (Integer) entry.getValue();
                result += key + ":" + value + "    ";
            }
            result += "\n";
            //键盘密码的模式分析
            result += "键盘密码\n";
            result += "no_pattern:" + keyboardPattern[0] + "    same_row:" + keyboardPattern[1] + "    zig_zag:" + keyboardPattern[2] +
                    "    snake:" + keyboardPattern[3] + "    same_row_number_only:" + keyboardPattern[4] + "\n";
            //日期密码格式分析
            result += "日期格式分析\n";
            result += "no_date:" + datePattern[0] + "    YYYYMMDD:" + datePattern[1] + "    MMDDYYYY:" + datePattern[2] + "    DDMMYYYY:" + datePattern[3] +
                    "    YYMMDD:" + datePattern[4] + "    MMDDYY:" + datePattern[5] + "    DDMMYY:" + datePattern[6] + "\n";
            //日期密码的构成
            result += "日期密码构成\n";
            result += "digit_only:" + passDatePattern[0] + "    letter_digit:" + passDatePattern[1] + "    symbol_digit:" + passDatePattern[2] + "    letter_digit_symbol:" + passDatePattern[3] + "\n";
            //拼音Top10
            result += "拼音Top10\n";
            Map<String, Integer> pinyinReusltMap = findTop10("pinyin");
            Iterator pinyinEntries = pinyinMap.entrySet().iterator();
            while(pinyinEntries.hasNext()) {
                Map.Entry entry = (Map.Entry)pinyinEntries.next();
                String key = (String) entry.getKey();
                int value = (Integer) entry.getValue();
                result += key + ":" + value + "    ";
            }
            result += "\n";
            //英文单词Top10
            result += "英文单词Top10\n";
            Map<String, Integer> englishReusltMap = findTop10("english");
            Iterator englishEntries = englishMap.entrySet().iterator();
            while(englishEntries.hasNext()) {
                Map.Entry entry = (Map.Entry)englishEntries.next();
                String key = (String) entry.getKey();
                int value = (Integer) entry.getValue();
                result += key + ":" + value + "    ";
            }
            result += "\n";
            //单词组成分析
            result += "单词密码构成\n";
            result += "pinyin_letter_only:" + wordsPattern[0] + "    english_letter_only:" + wordsPattern[1] + "    pinyin_mixed:" + wordsPattern[2] + "    english_mixed:" + wordsPattern[3] +
                    "    pinyin_uppercase_letter:" + cases[0] + "    english_uppercase_letter:" + cases[1] + "\n";
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
