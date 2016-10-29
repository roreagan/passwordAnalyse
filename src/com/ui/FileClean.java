package com.ui;

import java.io.*;

/**
 * Created by lgluo on 2016/10/29.
 */
public class FileClean {

    public static void main(String[] args){
        String filename = "yahoo";
        File file = new File(filename);
        File file1 = new File(filename + "1");
        BufferedReader reader = null;
        BufferedWriter writer = null;
        int count = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            writer = new BufferedWriter(new FileWriter(file1));
            String tempString = null;
            while((tempString = reader.readLine()) != null) {
                if(count % 100000 == 0) {
                    System.out.println(count);
                }
                String result = "";
                for(int i = 0; i < tempString.length(); i++) {
                    if(tempString.charAt(i) < 256) {
                        result += tempString.charAt(i);
                    }
                }
                if(result.length() < 6) {
                    continue;
                }
                writer.write(result + "\n");
                count++;
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
