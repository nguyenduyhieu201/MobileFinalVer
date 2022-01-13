package com.example.mobiledictionary.English;

import java.io.Serializable;

public class EnglishWord implements Serializable {
    private int id;
    private String word;
    private String meaning;
    private int highlight;
    private String note;

    public EnglishWord (int id, String word, String meaning, int highlight, String note)
    {
        this.id = id;
        this.word = word;
        this.meaning = meaning.replace(Character.toString((char) 94).charAt(0),Character.toString((char) 39).charAt(0));
        this.highlight = highlight;
        this.note = note;
    }
    public EnglishWord (String word, String meaning) {
        this.word = word;
        this.meaning = meaning.replace(Character.toString((char) 94).charAt(0),Character.toString((char) 39).charAt(0));
    }
    public int getId () {
        return id;
    }
    public String getWord () {
        return word;
    }
    public String getMeaning () {
        return meaning.replace(Character.toString((char) 94).charAt(0),Character.toString((char) 39).charAt(0));
    }
    public int getHighlight() {
        return highlight;
    }
    public String getNote () {
        return note;
    }
    public void setId (int id) {
        this.id = id;
    }

    public void setWord (String word) {
        this.word = word;
    }
    public void setMeaning (String meaning) {
        this.meaning = meaning;
    }
    public void setNote (String note) {
        this.note = note;
    }
    public void setHighlight (int highlight) {
        this.highlight = highlight;
    }
}
