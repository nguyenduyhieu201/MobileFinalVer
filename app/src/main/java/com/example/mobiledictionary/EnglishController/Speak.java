package com.example.mobiledictionary.EnglishController;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import android.speech.tts.TextToSpeech;

public class Speak extends Activity {
    public Speak() {

    }


//    private EditText editText;
//    private Button button;
    private boolean ready;
    public boolean getReady () {
        return ready;
    }
    public void setReady (boolean ready) {
        this.ready = ready;
    }
    public Locale getUserSelectedLanguage(String languageSelected) {
        if (languageSelected == "English") {
            Locale engUS = new Locale("en", "GB");
            return engUS;
        }
        else if (languageSelected == "VietNam") {
            Locale vietNam = new Locale("vi", "VN");
            return vietNam;
        }

        else {
            Locale engUK = new Locale("en","US");
            return engUK;
        }
    }

    public void setTextToSpeechLanguage(TextToSpeech textToSpeech, String languageSelected) {
        Locale language = getUserSelectedLanguage(languageSelected);

        if (language == null) {
            setReady(false);
            Toast.makeText(this, "Not language selected", Toast.LENGTH_SHORT).show();
            return;
        }
        int result = textToSpeech.setLanguage(language);
        if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA) {
            setReady(false);
            Toast.makeText(this, "Missing language data", Toast.LENGTH_SHORT).show();
            return;
        } else if (result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
            setReady(false);
//            Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show();
            return;
        } else {
            setReady(true);
            Locale currentLanguage = textToSpeech.getVoice().getLocale();
//            Toast.makeText(this, "Language " + currentLanguage, Toast.LENGTH_SHORT).show();
        }

    }

    public void speakOut(TextToSpeech textToSpeech, EditText editText, boolean ready) {
        if (!ready) {
//            Toast.makeText(this, "Text to Speech not ready", Toast.LENGTH_LONG).show();
            Log.d("error"," error");
            return;
        }
        // Text to Speak
        String toSpeak = editText.getText().toString();
//        Toast.makeText(this, toSpeak, Toast.LENGTH_SHORT).show();
        // A random String (Unique ID).
        String utteranceId = UUID.randomUUID().toString();
        textToSpeech.speak(toSpeak, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    public void printOutSupportedLanguages(TextToSpeech textToSpeech)  {
        // Supported Languages
        Set<Locale> supportedLanguages = textToSpeech.getAvailableLanguages();
        if(supportedLanguages!= null) {
            for (Locale lang : supportedLanguages) {
                Log.e("TTS", "Supported Language: " + lang);
            }
        }
    }
}
