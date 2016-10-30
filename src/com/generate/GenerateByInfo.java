package com.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lgluo on 2016/10/30.
 */
public class GenerateByInfo {

    private static GenerateByInfo instance = null;

    private Set<String> buffer = new HashSet<>();

    private GenerateByInfo() {

    }

    public static GenerateByInfo getInstance() {
        if(instance == null) {
            instance = new GenerateByInfo();
        }
        return instance;
    }

    private void generatePasswords(List<String> validInfos) {
        List<String> result = new ArrayList<>();
        for(int i = 1; i < (2 <<(validInfos.size()-1)); i++) {
            List<String>temp = new ArrayList<>();
            int tempNum = i;
            for(int j = validInfos.size()-1; j >= 0; j--) {
                int tempN = 2 << (j-1);
                tempN = tempN == 0 ? 1 : tempN;
                if(tempNum >= tempN) {
                    temp.add(validInfos.get(j));
                    tempNum -= tempN;
                }
            }
            add(temp, "");
        }
    }

    private void add(List<String> temp, String result) {
        if(temp.size() == 0) {
            buffer.add(result);
            return;
        }
        for(int i = 0; i < temp.size(); i++) {
            List<String> temp1 = new ArrayList<>(temp);
            String temp2 = temp.get(i) + result;
            temp1.remove(i);
            add(temp1, temp2);
        }
    }

    public boolean generate(String info) {
        String[] infos= info.split("---");
        List<String> validInfos = new ArrayList<>();
        for(int i = 0; i < infos.length; i++) {
            infos[i] = infos[i].substring(1);
            if(!infos[i].equals("")) {
                validInfos.add(infos[i]);
            }
        }
        if(validInfos.size() == 0) {
            return false;
        }
        validInfos.remove(0);
        String date = infos[0];
        List<String> dates = new ArrayList<>();
        if(!date.equals("")) {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);
            dates.add(year + month + day);
            dates.add(month + day + year);
            dates.add(day + month + year);
            dates.add(year.substring(2) + month + day);
            dates.add(month + day + year.substring(2));
            dates.add(day + month +  year.substring(2));
        }
        if(dates.size() == 0) {
            generatePasswords(validInfos);
        } else {
            for(int i = 0; i < dates.size(); i++) {
                List<String> test = new ArrayList<>(validInfos);
                test.add(dates.get(i));
                generatePasswords(test);
            }
        }
        File file = new File("libByInput.txt");
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(file));
            for (String password: buffer) {
                if(password.length() >= 6 && password.length() <= 20) {
                    writer.write(password + "\n");
                }
            }
            writer.close();
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
