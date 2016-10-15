package com.Analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgluo on 2016/10/15.
 */
public class WordNode {
    //todo changing Map to List can accelerate the searching
    public Map<Character, WordNode> nodeList = new HashMap<>();
    public boolean isValue;
    public int num = 0;
    public char c;

    public WordNode() {

    }

    public WordNode(char c) {
        this.c = c;
    }
}
