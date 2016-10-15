package com.Analysis;

import com.state.DateState;

import java.util.Date;

/**
 * Created by lgluo on 2016/10/15.
 */
public class DateFormat {
    private static WordNode rootNode = new WordNode();

    private static DateFormat dateFormat = null;

    private static int[] ambiguity = new int[8];

    private DateFormat() {
        String[] falseDate = {"111111", "123123", "111000", "112233",
                "100200", "111222", "121212", "520520", "110110", "123000", "101010", "111333",
                "110120", "102030", "110119", "121314", "521125", "120120", "010203", "122333",
                "121121", "101101", "131211", "100100", "321123", "110112", "112211", "111112",
                "520521", "110111"};
        for(int i = 0; i < falseDate.length; i++) {
            String temp = falseDate[i];
            WordNode node = rootNode;
            for(int j = 0; j < temp.length(); j++) {
                if(node.nodeList.get(temp.charAt(j)) != null) {
                    node = node.nodeList.get(temp.charAt(j));
                } else {
                    node.nodeList.put(temp.charAt(j), new WordNode());
                    node = node.nodeList.get(temp.charAt(j));
                }
            }
        }
    }

    public static DateFormat getInstance() {
        if(dateFormat == null) {
            dateFormat = new DateFormat();
        }
        return dateFormat;
    }

    private boolean isFalseDate(String date){
        WordNode node = rootNode;
        boolean result = true;
        for(int i = 0; i < date.length(); i++) {
            if(node.nodeList.get(date.charAt(i)) == null) {
                result = false;
                break;
            }
        }
        return result;
    }

    public int dateAnalyse(String date) {
        DateState result = DateState.NO_DATE;
        if(date.length() ==  6) {
            if(isFalseDate(date)) {
                return result.getDate();
            }
        }
        if(date.length() == 6) {
            String num12 = date.substring(0,2);
            int n12 = Integer.valueOf(num12);
            String num34 = date.substring(2,4);
            int n34 = Integer.valueOf(num34);
            String num56 = date.substring(4);
            int n56 = Integer.valueOf(num56);
            int count = 0;
            //YYMMDD
            if( (n34 > 0 && n34 < 13) && (n56 < 32)) {
                count += 1;
                result = DateState.YYMMDD;
            }
            //MMDDYY
            if((n12 > 0 && n12 < 13) && n34 < 32) {
                count += 10;
                result = DateState.MMDDYY;
            }
            //DDMMYY
            if(n12 < 32 && (n34 > 0 && n34 < 13)) {
                count += 100;
                result = DateState.DDMMYY;
            }
            switch (count) {
                case 11:
                    ambiguity[0]++;
                    result = DateState.TEMP;
                    break;
                case 101:
                    ambiguity[1]++;
                    result = DateState.TEMP;
                    break;
                case 110:
                    ambiguity[2]++;
                    result = DateState.TEMP;
                    break;
                case 111:
                    ambiguity[3]++;
                    result = DateState.TEMP;
                    break;
            }
        } else {
            String num1234 = date.substring(0,4);
            int n1234 = Integer.valueOf(num1234);
            String num56 = date.substring(4,6);
            int n56 = Integer.valueOf(num56);
            String num78 = date.substring(6);
            int n78 = Integer.valueOf(num78);
            int count = 0;
            //YYYYMMDD
            if( (n1234 >= 1900 && n1234 <= 2099) && (n56 > 0 && n56 <13) && n78 < 32) {
                count += 1;
                result = DateState.YYYYMMDD;
            }
            String num12 = date.substring(0,2);
            int n12 = Integer.valueOf(num12);
            String num34 = date.substring(2,4);
            int n34 = Integer.valueOf(num34);
            String num5678 = date.substring(4);
            int n5678 = Integer.valueOf(num5678);
            //MMDDYYYY
            if((n12 > 0 && n12 < 13) && n34 < 32 && (n5678 >= 1900 && n5678 <= 2099)) {
                count += 10;
                result = DateState.MMDDYYYY;
            }
            //DDMMYYYY
            if(n12 < 32 && (n34 > 0 && n34 < 13) && (n5678 >= 1900 && n5678 <= 2099)) {
                count += 100;
                result = DateState.DDMMYYYY;
            }
            switch (count) {
                case 11:
                    ambiguity[4]++;
                    result = DateState.TEMP;
                    break;
                case 101:
                    ambiguity[5]++;
                    result = DateState.TEMP;
                    break;
                case 110:
                    ambiguity[6]++;
                    result = DateState.TEMP;
                    break;
                case 111:
                    ambiguity[7]++;
                    result = DateState.TEMP;
                    break;
            }
        }
        return result.getDate();
    }
}
