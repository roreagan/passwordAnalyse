package com.ui;

import java.io.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by lgluo on 2016/10/29.
 */
public class FileClean {
    private class pair {
        String key;
        int num = 0;

        public pair(String s, int n) {
            key = s;
            num = n;
        }
    }

    public void sort() {
        String filename = "163mail.log";
        File file = new File(filename);
        BufferedReader reader = null;
        PriorityQueue<pair> queue = new PriorityQueue<>(new Comparator<pair>() {
            @Override
            public int compare(pair o1, pair o2) {
                if(o1.num > o2.num) {
                    return 1;
                } else if(o1.num == o2.num) {
                    return 0;
                } else {
                 return -1;
                }
            }
        });
        try{
            reader = new BufferedReader(new FileReader(file));
            int count = 0;
            String tempString = null;
            while((tempString = reader.readLine()) != null) {
                count++;
                if(count == 20) {
                    String[] mapString = tempString.split("    ");
                    for(int i = 0; i < mapString.length; i++) {
                        queue.add(new pair(mapString[i].split(":")[0], Integer.valueOf(mapString[i].split(":")[1])));
                    }
                }
            }
            while(true) {
                if(queue.size() == 0) {
                    break;
                }
                pair p = queue.poll();
                System.out.println(p.key + "  " + ((float)p.num/334229));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        FileClean f = new FileClean();
        f.sort();
//        String filename = "yahoo";
//        File file = new File(filename);
//        File file1 = new File(filename + "1");
//        BufferedReader reader = null;
//        BufferedWriter writer = null;
//        int count = 0;
//        try {
//            reader = new BufferedReader(new FileReader(file));
//            writer = new BufferedWriter(new FileWriter(file1));
//            String tempString = null;
//            while((tempString = reader.readLine()) != null) {
//                if(count % 100000 == 0) {
//                    System.out.println(count);
//                }
//                String result = "";
//                for(int i = 0; i < tempString.length(); i++) {
//                    if(tempString.charAt(i) < 256) {
//                        result += tempString.charAt(i);
//                    }
//                }
//                if(result.length() < 6) {
//                    continue;
//                }
//                writer.write(result + "\n");
//                count++;
//            }
//            writer.close();
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Set<String> passwords = new HashSet<>();
//        String filename = "163mail.txt";
//        File file = new File(filename);
//        File file1 = new File("1" + filename);
//        BufferedReader reader = null;
//        BufferedWriter writer = null;
//        int count = 0;
//        try {
//            reader = new BufferedReader(new FileReader(file));
//            writer = new BufferedWriter(new FileWriter(file1));
//            String tempString = null;
//            while((tempString = reader.readLine()) != null) {
//                if(count % 100000 == 0) {
//                    System.out.println(count);
//                }
//                String prefix = tempString.split("----")[0];
//                String result = tempString.split("----")[1];
//                if(result.length() < 6) {
//                    continue;
//                }
//                String result1 = "";
//                for(int i = 0; i < result.length(); i++) {
//                    if(result.charAt(i) != ' ') {
//                        result1 += result.charAt(i);
//                    }
//                }
//                if(!passwords.contains(prefix)) {
//                    writer.write(result1 + "\n");
//                    passwords.add(prefix);
//                }
//                count++;
//            }
//            writer.close();
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
