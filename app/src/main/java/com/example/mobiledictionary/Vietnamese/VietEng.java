package com.example.mobiledictionary.Vietnamese;

import android.app.Dialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledictionary.EnglishController.Speak;
import com.example.mobiledictionary.EnglishController.WordController;
import com.example.mobiledictionary.R;
import com.example.mobiledictionary.WordHelper.WordHelper;

public class VietEng extends AppCompatActivity {
    private Button mVietSearchButton;
    private TextView engMean;
    private TextView vietWord;
    private EditText vietSearchWord;
    private CompoundButton mButtonVietWordHighlight;
    private Button mButtonOpen_Dialog_VietWord_Note;
    private Button mButtonVietSpeak;
    private TextToSpeech textVietToSpeech;
    private WordHelper vietWordHelper = new WordHelper(this,
            null, 1);
    private int vietWordId = 0;
    private WordController vietWordController = new WordController();
    private Speak speak = new Speak();
    public VietEng () {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viet_anh);
        mButtonOpen_Dialog_VietWord_Note = findViewById(R.id.button_open_VietWord_dialog_note);
        mVietSearchButton = findViewById(R.id.bViet_search);
        vietWord = findViewById(R.id.vietWord);
        engMean = findViewById(R.id.EngMeaning);
        vietSearchWord = findViewById(R.id.Vietedittext);
        mButtonVietWordHighlight = findViewById(R.id.buttonVietWordHighlight);
        mButtonOpen_Dialog_VietWord_Note = findViewById(R.id.button_open_VietWord_dialog_note);
        mButtonVietSpeak = findViewById (R.id.button_VietSpeak);
        mButtonVietWordHighlight.setVisibility(View.GONE);
        mButtonOpen_Dialog_VietWord_Note.setVisibility(View.GONE);
        mButtonVietSpeak.setVisibility(View.GONE);
        textVietToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speak.setTextToSpeechLanguage(textVietToSpeech,"VietNam");
            }
        });
//        vietWordHelper.CreateData("VietEngDemo");
//        vietWordHelper.InsertData("VietEngDemo","xin chao","hi");
//        vietWordHelper.InsertData("VietEngDemo","xin chao 2","hello");
//        vietWordHelper. InsertData("VietEngDemo","meo","cat");
//        vietWordHelper.InsertData("VietEngDemo","cho","dog");
        mVietSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vietWordId = vietWordController.search(vietSearchWord, vietWord, vietWordHelper,
                        "VietEngDemo", engMean, mButtonVietWordHighlight,
                        mButtonOpen_Dialog_VietWord_Note,mButtonVietSpeak);
//                Log.d("id search VA", String.valueOf(vietWordId));

            }
        });

        //highlight từ vựng
//        vietWordController.HighlightAWord(mButtonVietWordHighlight,vietWordHelper,vietWordId,
//                "VietEngDemo");
        mButtonVietWordHighlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((ToggleButton) v).isChecked();
                //checked = true thi highlight = 1
                if (checked){
                    vietWordHelper.HighlightWord(vietWordId,"VietEngDemo");
                    Log.d("id", String.valueOf(vietWordId));
                    mButtonVietWordHighlight.setButtonDrawable(R.drawable.icon_star);
                }
                //checked = false thi highlight = 0
                else{
                    vietWordHelper.UnHighlightWord(vietWordId,"VietEngDemo");
                    Log.d("id", String.valueOf(vietWordId));
                    mButtonVietWordHighlight.setButtonDrawable(R.drawable.icon_star_48);
                }
            }
        });

        mButtonOpen_Dialog_VietWord_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_Dialog_Note(vietWordId, Gravity.CENTER);
            }
        });

        mButtonVietSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dang noi", "dang noi");
                speak.setTextToSpeechLanguage(textVietToSpeech,"VietNam");
                speak.speakOut(textVietToSpeech, vietSearchWord,speak.getReady());
            }
        });
    }

    //search từ


    //mở nơi để note
    private void open_Dialog_Note (int idWord, int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_note);
        Window window = dialog.getWindow();
        if(window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }

        EditText edittext_note = dialog.findViewById(R.id.edit_text_note);
        Button button_add_note = dialog.findViewById(R.id.button_add_note);
        Button button_cancer_dialog_note = dialog.findViewById(R.id.button_cancer_note);

        String note_1 = vietWordHelper.getNoteWord(idWord, "VietEngDemo");
        if (note_1 == "") {
        } else {
            edittext_note.setText(note_1, TextView.BufferType.EDITABLE);
        }

        button_cancer_dialog_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        button_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_add_note.setAlpha((float) 0.4);
                String note = edittext_note.getText().toString().trim();
                vietWordHelper.NoteWord(note, idWord, "VietEngDemo");
            }
        });
        dialog.show();
    }
}
