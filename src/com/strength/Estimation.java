package com.strength;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by lgluo on 2016/10/24.
 */
public class Estimation {
    public final int GRAMLEN = 3;
    public final double miniP = 0.001;
    //用来过滤用的比较少的字符来节省内存
    private final HashSet<Character> filtedChar = new HashSet<>();
    private Map<String, Gram> nGramMap = new HashMap<>();

    private static Estimation estimation = null;
    private static String libName = "100line.txt";

    private Estimation() {
        //cipher database
        File readFile = new File(libName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(readFile));
            String tempString = null;
            while((tempString = reader.readLine()) != null) {
                String process1 = estimation.filter(tempString);
                for(int i = 0; i + estimation.GRAMLEN < process1.length(); i++) {
                    estimation.addGram(process1.substring(i, i + estimation.GRAMLEN), process1.charAt(i + estimation.GRAMLEN));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Estimation getInstance() {
        if(estimation == null) {
            estimation = new Estimation();
        }
        return estimation;
    }

    private class Gram {
        public Map<Character, Integer> nextChar = new HashMap<>();
        public int num;

        public Gram(char c) {
            this.num = 1;
            this.nextChar.put(c, 1);
        }
    }

    public String filter(String password) {
        String result = "";
        for(int i = 0; i < password.length(); i++) {
            if(filtedChar.contains(password.charAt(i))){
                continue;
            }
            result += password.charAt(i);
        }
        return result;
    }

    public void addGram(String portion, char nextc) {
        int num = 1;
        Gram gram = nGramMap.get(portion);
        if(gram != null) {
            int charNum = 1;
            if(gram.nextChar.get(nextc) != null) {
                charNum += gram.nextChar.get(nextc);
            }
            gram.nextChar.put(nextc, charNum);
            gram.num++;
        } else if(nGramMap.get(portion) == null) {
            gram = new Gram(nextc);
        }
        nGramMap.put(portion, gram);
    }

    public double checkStrength(String password) {
        double probability = 1.0;
        for(int i = 0; i + GRAMLEN < password.length(); i++) {
            Gram gram = nGramMap.get(password.substring(i, i + GRAMLEN));
           if(gram != null) {
               if(gram.nextChar.get(password.charAt(i + GRAMLEN)) != null) {
                   probability *= gram.nextChar.get(password.charAt(i + GRAMLEN)) / gram.num;
               } else {
                   probability *= miniP;
               }
           } else {
               probability *= miniP;
           }
        }
        return (-Math.log(probability)) > 100.0? 100 : -Math.log(probability);
    }

    public void add(String password) {
        String process1 = filter(password);
        for(int i = 0; i + GRAMLEN < process1.length(); i++) {
            addGram(process1.substring(i, i + GRAMLEN), process1.charAt(i + GRAMLEN));
        }
    }

//    public static void main(String args[]) {
//        Estimation estimation = new Estimation();
//        //cipher database
//        File readFile = new File("100line.txt");
//        File writeFile = new File("100output");
//        BufferedReader reader = null;
//        BufferedWriter writer = null;
//        try {
//            reader = new BufferedReader(new FileReader(readFile));
//            writer = new BufferedWriter(new FileWriter(writeFile));
//            String tempString = null;
//            while((tempString = reader.readLine()) != null) {
//                String process1 = estimation.filter(tempString);
//                for(int i = 0; i + estimation.GRAMLEN < process1.length(); i++) {
//                    estimation.addGram(process1.substring(i, i + estimation.GRAMLEN), process1.charAt(i + estimation.GRAMLEN));
//                }
//            }
//            writer.write(estimation.nGramMap.toString());
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
