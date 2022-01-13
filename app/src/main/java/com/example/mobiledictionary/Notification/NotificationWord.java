package com.example.mobiledictionary.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledictionary.English.EnglishWord;
import com.example.mobiledictionary.R;

import org.w3c.dom.Text;

public class NotificationWord extends AppCompatActivity {
    private String wordReceive;
    private String meaningReceive;
    private TextView wordField;
    private TextView meaningField;

    public NotificationWord() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notification);
        wordField = findViewById(R.id.edittext);
        meaningField = findViewById(R.id.meaning);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){

            EnglishWord englishWord = (EnglishWord) bundle.getSerializable("Serializable");
            wordField.setText(englishWord.getWord());
            meaningField.setText(englishWord.getMeaning());
        }

        SharedPreferences sharedPref = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String wordReceive = sharedPref.getString("hlWord","");
        String meaningReceive = sharedPref.getString("hlMean", "");
        wordField.setText(wordReceive);
        meaningField.setText(meaningReceive);

    }
}



//        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE) ;
//        // Intent part
//        Intent alarmIntent = new Intent(this, Receiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//        int interval = 1;
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);