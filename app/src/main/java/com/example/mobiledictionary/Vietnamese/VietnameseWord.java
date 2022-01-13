package com.example.mobiledictionary.Vietnamese;

public class VietnameseWord {
        private int vietId;
        private String vietWord;
        private String vietMeaning;
        private int vietHighlight;
        private String vietNote;

        public VietnameseWord (int id, String word, String meaning, int highlight, String note)
        {
            this.vietId = id;
            this.vietWord = word;
            this.vietMeaning = meaning;
            this.vietHighlight = highlight;
            this.vietNote = note;
        }

        public int getId () {
            return vietId;
        }
        public String getWord () {
            return vietWord;
        }
        public String getMeaning () {
            return vietMeaning;
        }
        public int getHighlight() {
            return vietHighlight;
        }
        public String getNote () {
            return vietNote;
        }
        public void setId (int id) {
            this.vietId = id;
        }

        public void setWord (String word) {
            this.vietWord = word;
        }
        public void setMeaning (String meaning) {
            this.vietMeaning = meaning;
        }
        public void setNote (String note) {
            this.vietNote = note;
        }
        public void setHighlight (int highlight) {
            this.vietHighlight = highlight;
        }
}
