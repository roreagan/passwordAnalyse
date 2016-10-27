package com.Analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by lgluo on 2016/10/15.
 */
public class WordsMatch {
    private static WordsMatch wordsMatch;
    private static WordNode chineseNode = new WordNode();
    private static WordNode englishNode = new WordNode();

    private WordsMatch(){
        generateTire(chineseNode, "pinyin");
        generateTire(englishNode, "english");
    }

    private void generateTire(WordNode rootNode, String filename) {
        File file = new File(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            WordNode node = rootNode;
            while((tempString = reader.readLine()) != null) {
                tempString = tempString.toLowerCase();
                for(int i = 0; i < tempString.length(); i++) {
                    if(node.nodeList.get(tempString.charAt(i)) != null) {
                        node = node.nodeList.get(tempString.charAt(i));
                    } else {
                        WordNode tempNode = new WordNode();
                        node.nodeList.put(tempString.charAt(i), tempNode);
                        node = tempNode;
                    }
                }
                node.isValue = true;
                node = rootNode;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WordsMatch getInstance() {
        if(wordsMatch == null) {
            wordsMatch = new WordsMatch();
        }
        return wordsMatch;
    }

    //the input password should be letter-only
    public boolean identifyWord(String password, String language) {
        WordNode node;
        if(language.equals("pinyin")) {
            node = chineseNode;
        } else {
            node = englishNode;
        }
        for(int i = 0; i < password.length(); i++) {
            if(node.nodeList.get(password.charAt(i)) == null) {
                if(i == 0 || !node.isValue) {
                    return false;
                }
                return identifyWord(password.substring(i), language);
            } else {
                node = node.nodeList.get(password.charAt(i));
                if(node.isValue && identifyWord(password.substring(i+1), language)) {
                    return true;
                }
            }
        }
        return node.isValue;
    }

    public String generatePassword(boolean isPinyin) {
        String result = "";
        WordNode root = isPinyin ? chineseNode : englishNode;
        Random ra = new Random();
        int length = ra.nextInt(10) + 6;
        while(result.length() < length){
            WordNode node = root;
            String word = "";
            while(!(node.nodeList.size() == 0 || (node.isValue && ra.nextInt(2)==1))) {
                int nextNum = ra.nextInt(node.nodeList.size());
                int i = 0;
                for(Map.Entry entry : node.nodeList.entrySet()){
                    if(i == nextNum) {
                        word += (Character)entry.getKey();
                        node = (WordNode) entry.getValue();
                        break;
                    }
                    i++;
                }
            }
            result += word;
        }
        return result;
    }
}
