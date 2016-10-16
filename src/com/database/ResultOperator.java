package com.database;

import com.Analysis.WordNode;

import java.util.*;

/**
 * Created by lgluo on 2016/10/15.
 */
public class ResultOperator {
    private static WordNode passwordNode = new WordNode();
    private static WordNode structureNode = new WordNode();
    private static WordNode englishNode = new WordNode();
    private static WordNode pinyinNode = new WordNode();

    private static Map<String, Integer> passwordMap = new HashMap<>();
    private static Map<String, Integer> structureMap = new HashMap<>();
    private static Map<String, Integer> englishMap = new HashMap<>();
    private static Map<String, Integer> pinyinMap = new HashMap<>();

    private static Map<Character, Integer> characterIntegerMap = new HashMap();
    //relate to KeyboardState
    private static long[] keyboardPattern = {0, 0, 0, 0, 0};
    //0: pinyin_letter only   1: english_letter only   2: pinyin_mix    3:english_mix
    private static long[] wordsPattern = {0, 0, 0, 0};
    //relate to DateState
    private static long[] datePattern = {0, 0, 0, 0, 0, 0, 0};
    //relate to DatePattern
    private static long[] passDatePattern = {0, 0, 0, 0};

    private static ResultOperator resultOperator = null;

    private ResultOperator() {

    }

    public static ResultOperator getInstance() {
        if(resultOperator == null) {
            resultOperator = new ResultOperator();
        }
        return resultOperator;
    }

    //各个字符的使用频率
    public void analyseCharacter(Map<Character, Integer> map) {
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
            if(map.size() < 10) {
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

    public void addWordsPattern(boolean isPinyin, boolean isEnglish, boolean letterOnly, String word) {
        if(isPinyin || isEnglish){
            if(isPinyin) {
                if(letterOnly) {
                    wordsPattern[0]++;
                } else {
                    wordsPattern[2]++;
                }
                add(word, "pinyin");
            } else {
                if(letterOnly) {
                    wordsPattern[1]++;
                } else {
                    wordsPattern[3]++;
                }
                add(word, "english");
            }
        }
    }

    public void addDatePattern(int pattern, int passPattern) {
        datePattern[pattern]++;
        passDatePattern[passPattern]++;
    }

    public void addRestDatePattern(long[] patterns) {
        datePattern[0] += patterns[0] * datePattern[0] / (datePattern[0] + datePattern[1]) + patterns[1] * datePattern[0] / (datePattern[0] + datePattern[2]) +  patterns[3] * datePattern[0] / (datePattern[0] + datePattern[1] + datePattern[2]);
        datePattern[1] += patterns[0] * datePattern[1] / (datePattern[0] + datePattern[1]) + patterns[2] * datePattern[1] / (datePattern[1] + datePattern[2]) +  patterns[3] * datePattern[1] / (datePattern[0] + datePattern[1] + datePattern[2]);
        datePattern[2] += patterns[1] * datePattern[2] / (datePattern[0] + datePattern[2]) + patterns[2] * datePattern[2] / (datePattern[1] + datePattern[2]) +  patterns[3] * datePattern[2] / (datePattern[0] + datePattern[1] + datePattern[2]);
        datePattern[3] += patterns[4] * datePattern[3] / (datePattern[3] + datePattern[4]) + patterns[5] * datePattern[3] / (datePattern[3] + datePattern[5]) +  patterns[7] * datePattern[0] / (datePattern[3] + datePattern[4] + datePattern[5]);
        datePattern[4] += patterns[4] * datePattern[4] / (datePattern[3] + datePattern[4]) + patterns[6] * datePattern[4] / (datePattern[4] + datePattern[5]) +  patterns[7] * datePattern[1] / (datePattern[3] + datePattern[4] + datePattern[5]);
        datePattern[5] += patterns[5] * datePattern[5] / (datePattern[3] + datePattern[5]) + patterns[6] * datePattern[5] / (datePattern[4] + datePattern[5]) +  patterns[7] * datePattern[2] / (datePattern[3] + datePattern[4] + datePattern[5]);
    }

}
