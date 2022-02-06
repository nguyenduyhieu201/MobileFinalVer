package com.example.mobiledictionary.English;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledictionary.EnglishController.Speak;
import com.example.mobiledictionary.EnglishController.WordController;
import com.example.mobiledictionary.TranslateOnline.TranslateOnline;
import com.example.mobiledictionary.WordHelper.WordHelper;
import com.example.mobiledictionary.MainActivity;
import com.example.mobiledictionary.R;

import android.speech.tts.TextToSpeech;



public class EngViet extends AppCompatActivity {

    private TextView mTextMessage;
    private Button mButton;
    private Button mEngSpeakButton;
    private EditText mEditText;
    private Button mEngSpeak;
    int flag = 0;
    private TextView word;
    private EditText search_1;
    private LinearLayout lineShowMeanWord;
    //String word = "";
    int idWord = 0;
    private CompoundButton mButtonHighlight;
    private Button mButtonOpen_Dialog_Note;
    private WordHelper englishWordHelper = new WordHelper(this,
            null, 1);
    private String key1;
    private TextView meaning;
    private WordController englishWordController = new WordController();
    private TextToSpeech textToSpeech;
    private Speak speak = new Speak();
    private boolean ready;
    public String languageSelected;
    public String speedSpeak;

    public EngViet () {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.activity_main);
        //gán giá trị cho các biến
        mButtonOpen_Dialog_Note = findViewById(R.id.button_open_dialog_note);
        lineShowMeanWord =  findViewById(R.id.line_show_mean_word);
        word =  findViewById(R.id.word);
        mEngSpeak = findViewById(R.id.button_EngSpeak);
        mButtonHighlight = (ToggleButton) findViewById(R.id.buttonHighlight);
        mButtonOpen_Dialog_Note = findViewById(R.id.button_open_dialog_note);
        mButton = findViewById(R.id.bAnh_search);
        mButtonHighlight.setVisibility(View.GONE);
        mButtonOpen_Dialog_Note.setVisibility(View.GONE);
        search_1 = findViewById(R.id.edittext);
        meaning = findViewById(R.id.meaning);
        mEngSpeakButton = findViewById(R.id.button_EngSpeak);
        mEngSpeakButton.setVisibility(View.GONE);
        mButton.setEnabled(true);
        SharedPreferences sharedPref = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        languageSelected = sharedPref.getString("accent","");
        speedSpeak = sharedPref.getString("speedSelect", "");

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speak.setTextToSpeechLanguage(textToSpeech,"VietNam");
            }
        });
        if (speedSpeak.equals("fast")) {
            textToSpeech.setSpeechRate((float) 1.5);
            editor.putBoolean("selectFast", true);
            editor.apply();
        }
        else {
            textToSpeech.setSpeechRate((float) 1.0);
            editor.putBoolean("selectFast", false);
            editor.apply();
        }

        //tìm kiếm từ vựng

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        search_1.setText(text);
        if (text != null)  {
            idWord = englishWordController.search(search_1,word,englishWordHelper,
                    "NoiDung",meaning, mButtonHighlight, mButtonOpen_Dialog_Note,
                    mEngSpeakButton);
        }



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                idWord = search(englishWordHelper,"NoiDung");
                idWord = englishWordController.search(search_1,word,englishWordHelper,
                        "NoiDung",meaning, mButtonHighlight, mButtonOpen_Dialog_Note,
                        mEngSpeakButton);
            }
        });

        //highlight từ vựng
        englishWordController.HighlightAWord(mButtonHighlight, englishWordHelper,idWord,
                "NoiDung");


        //mở đoạn ghi chú
        mButtonOpen_Dialog_Note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_Dialog_Note(idWord, Gravity.CENTER);
            }
        });

        //hàm phát âm
        mEngSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("accent select", languageSelected);
                Log.d("Speed", speedSpeak);
                if (languageSelected.equals("English")) {
                    speak.setTextToSpeechLanguage(textToSpeech, "English");
                } else {
                    speak.setTextToSpeechLanguage(textToSpeech, "EngUS");

                }
                speak.speakOut(textToSpeech, search_1,speak.getReady());
            }
        });
    }

    // hàm tìm kiếm từ vựng

    //mở đoạn dialog khi ấn vào nút
    private void open_Dialog_Note (int idWOrd, int gravity){
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

        String note_1 = englishWordHelper.getNoteWord(idWord, "NoiDung");
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
                String note = edittext_note.getText().toString().trim();
                button_add_note.setAlpha((float) 0.4);
                englishWordHelper.NoteWord(note, idWord, "NoiDung");
//                dialog.dismiss();
                Toast.makeText(EngViet.this, "Thêm ghi chú thành công.", Toast.LENGTH_SHORT);
            }
        });
        dialog.show();
    }

    public void setAccent (String languageSelected) {
        this.languageSelected = languageSelected;
    }
}

//      englishWordHelper.CreateData("NoiDung");
//       englishWordHelper.InsertData("NoiDung","hi","xin chao");
//      englishWordHelper.InsertData("NoiDung","hello","xin chao 2");
//      englishWordHelper. InsertData("NoiDung","cat","meo");
//       englishWordHelper.InsertData("NoiDung","dog","cho");

//
//    public int search(WordHelper englishWordHelper, String tableName) {
//        InputMethodManager inputManager = (InputMethodManager)
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        String key1 = search_1.getText().toString().trim();
//        word.setText(key1);
//        TextView meaning= findViewById(R.id.meaning);
//        Cursor meaningCursor = englishWordHelper.SearchWord(key1, tableName);
//
//        int idWord = 0;
//        String value = "";
//        int count = meaningCursor.getCount();
//        if (meaningCursor.getCount() > 0) {
//            meaningCursor.moveToNext();
//            value = meaningCursor.getString(2);
//            idWord = meaningCursor.getInt(0);
//        }
//        else value = null;
//        if (value == null) {
//            meaning.setText("Khong co tu nao");
//        }
//        else {
//            mButtonHighlight.setVisibility(View.VISIBLE);
//            mButtonOpen_Dialog_Note.setVisibility(View.VISIBLE);
//            mButtonHighlight.setChecked(false);
//            if (englishWordHelper.getHighlightWord(idWord,"NoiDung") == 0) {
//                mButtonHighlight.setChecked(false);
//                mButtonHighlight.setButtonDrawable(R.drawable.icon_star_48);
//            }
//            else {
//                mButtonHighlight.setChecked(true);
//                mButtonHighlight.setButtonDrawable(R.drawable.icon_star);
//            }
//            meaning.setText(value);
//        }
//        return idWord;
//    }