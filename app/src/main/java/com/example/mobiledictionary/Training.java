package com.example.mobiledictionary;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledictionary.English.EnglishWord;
import com.example.mobiledictionary.EnglishController.WordController;
import com.example.mobiledictionary.WordHelper.WordHelper;

import java.util.List;

public class Training extends AppCompatActivity {
    private WordHelper wordHelper = new WordHelper(this,
            "TuDienSqlite", null, 1);
    private WordController wordController = new WordController();
    private List<EnglishWord> mListHighlight;
    private EnglishWord randomWord;
    private EditText wordInput;
    private TextView meaning;
    private Button check;
    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training);
        mListHighlight = wordController.getListHighlight(wordHelper, "NoiDung",
                "VietEngDemo");
        randomWord = wordController.getRandomWord(mListHighlight);
        wordInput = findViewById(R.id.wordInput);
        meaning = findViewById(R.id.meanOfRanWord);
        check = findViewById(R.id.check);
        play = findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordInput.setText("");
                meaning.setText(randomWord.getMeaning());
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = wordInput.getText().toString();
                if (input.equals(randomWord.getWord())) {
                    openAlertResult(true,randomWord.getWord());
                    randomWord = wordController.getRandomWord(mListHighlight);
                }
                else {
                    openAlertResult(false, randomWord.getWord());
                    randomWord = wordController.getRandomWord(mListHighlight);
                }
            }
        });
    }

    public void openAlertResult (boolean exact, String word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết quả");
        if (!exact) {
            builder.setIcon(R.drawable.ic_baseline_sms_failed_24);
            builder.setMessage("Bạn làm sai rồi, kết quả là " + word);
        }
        if (exact) {
            builder.setIcon(R.drawable.ic_baseline_check_24);
            builder.setMessage("Chúc mừng! Bạn đã làm đúng");
        }
        AlertDialog alert = builder.create();
        alert.show();
        randomWord = wordController.getRandomWord(mListHighlight);

    }
}
