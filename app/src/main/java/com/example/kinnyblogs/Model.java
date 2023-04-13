package com.example.kinnyblogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Model {
    String title, desc;
    ArrayList<HashMap<String, Object>> clues;

    public Model() {}

    public Model(String title, String desc, ArrayList<HashMap<String, Object>> clues){
        this.title = title;
        this.desc = desc;
        this.clues = clues;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<HashMap<String, Object>> getClues() {
        return clues;
    }

    public void setClues(ArrayList<HashMap<String, Object>> clues) {
        this.clues = clues;
    }
}
