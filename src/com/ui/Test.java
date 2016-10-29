package com.ui;

import com.Analysis.Analyse;
import com.generate.GenerateLibrary;

/**
 * Created by lgluo on 2016/10/29.
 */
public class Test {
    private String inputFileName;

    public void analyse(String filename) {
        if(filename.contains(".")) {
            inputFileName = filename.split(".")[0];
        } else {
            inputFileName = filename;
        }
        Analyse analyse = new Analyse();
        analyse.analyse(filename);
    }

    public void generateLibrary() {
        GenerateLibrary generateLibrary = new GenerateLibrary(inputFileName);
        generateLibrary.generatePassword();
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Test test = new Test();
        test.analyse("163mail");
        System.out.print(System.currentTimeMillis() - start);
    }
}
